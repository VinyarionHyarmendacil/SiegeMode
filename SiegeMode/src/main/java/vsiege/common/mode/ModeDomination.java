package vsiege.common.mode;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.Constants.NBT;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
import vsiege.common.addon.AddonHooks;
import vsiege.common.game.ZoneControl;

public class ModeDomination extends Mode {

	public List<ZoneControl> zones = Lists.newArrayList();

	public boolean tick() {
		for (ZoneControl zone : zones) {
			zone.tick();
			if (zone.occupiers != null) {
				zone.occupiers.score++;
			}
		}
		for (SiegeTeam team : siege.teams()) {
			if (team.score >= this.pointsNeededToWin) {
				return true;
			}
		}
		return false;
	}

	public String score(World world, Siege siege, SiegeTeam team) {
		return team.color + team.getTeamName() + EnumChatFormatting.GOLD + ": Score: " + team.score;
	}

	public boolean isReady() {
		return zones.size() > 0;
	}

	protected void fromNBT0(Siege siege, NBTTagCompound nbt) {
		NBTTagList czs = nbt.getTagList("VinyarionAddon_ControlZones", NBT.TAG_COMPOUND);
		zones.clear();
		for (int i = 0; i < czs.tagCount(); i++) {
			ZoneControl zone = new ZoneControl();
			zone.fromNBT(siege, czs.getCompoundTagAt(i));
			zones.add(zone);
		}
	}

	protected void toNBT0(Siege siege, NBTTagCompound nbt) {
		NBTTagList czs = new NBTTagList();
		for (ZoneControl zone : zones) {
			czs.appendTag(zone.toNBT(siege, new NBTTagCompound()));
		}
		nbt.setTag("VinyarionAddon_ControlZones", czs);
	}

	public int scoringMethod(Siege siege, SiegeTeam team) {
		return team.score;
	}

	public String endMessage(Siege siege, SiegeTeam team, String message) {
		return super.endMessage(siege, team, message) + ", Points: " + team.score;
	}

	protected String object() {
		return "point";
	}

	public void scoreboard(List<Score> list, Scoreboard board, ScoreObjective objective, String timeRemaining, SiegeTeam team, EntityPlayerMP entityplayer, SiegePlayerData playerdata) {
		list.add(null);
		list.add(new Score(board, objective, "Team points: " + team.score));
		for(ZoneControl zone : zones) {
			if(zone.box.intersectsWith(entityplayer.boundingBox) && team.getTeamName().equals(zone.attackers.getTeamName())) {
				list.add(new Score(board, objective, "Progress: " + String.valueOf(MathHelper.floor_double((double)zone.ticksHeld / (double)zone.ticksTillOccupation))));
			}
		}
	}
	
	public void printMVP(Siege siege2, List<SiegeTeam> siegeTeams) {
		UUID mvpID = null;
		int mvpKills = 0;
		int mvpDeaths = 0;
		int mvpScore = Integer.MIN_VALUE;
		UUID longestKillstreakID = null;
		int longestKillstreak = 0;
		for (SiegeTeam team : siegeTeams) {
			for (UUID player : team.getPlayerList()) {
				SiegePlayerData playerData = siege.getPlayerData(player);
				int kills = playerData.getKills();
				int deaths = playerData.getDeaths();
				int score = playerData.addonData.personalscore;
				if (score > mvpScore || (score == mvpScore && deaths < mvpDeaths)) {
					mvpID = player;
					mvpKills = kills;
					mvpDeaths = deaths;
					mvpScore = score;
				}
				int streak = playerData.getLongestKillstreak();
				if (streak > longestKillstreak) {
					longestKillstreakID = player;
					longestKillstreak = streak;
				}
			}
		}
		if (mvpID != null) {
			String mvp = UsernameCache.getLastKnownUsername(mvpID);
			AddonHooks.messageAllSiegePlayers(siege, "MVP was " + mvp + " (" + siege.getPlayerTeam(mvpID).getTeamName() + ") with " + mvpKills + " kills / " + mvpDeaths + " deaths / " + mvpScore + " occupation points");
		}
		if (longestKillstreakID != null) {
			String streakPlayer = UsernameCache.getLastKnownUsername(longestKillstreakID);
			AddonHooks.messageAllSiegePlayers(siege, "Longest killstreak was " + streakPlayer + " (" + siege.getPlayerTeam(longestKillstreakID).getTeamName() + ") with a killstreak of " + longestKillstreak);
		}
	}
	
	public void startSiege() {
		for(SiegeTeam team : siege.teams()) {
			team.score = team.antiscore = 0;
		}
		for(ZoneControl zone : zones) {
			zone.attackers = null;
			zone.occupiers = null;
			zone.ticksHeld = 0;
		}
	}

	public CommandException generateException() {
		return new CommandException("Siege %s cannot be started - it requires a location, at least one team, and at least one zone", siege.getSiegeName());
	}

}
