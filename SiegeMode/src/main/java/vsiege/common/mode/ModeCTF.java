package vsiege.common.mode;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.Constants.NBT;
import siege.common.kit.KitDatabase;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
import vsiege.common.addon.AddonHooks;
import vsiege.common.game.ZoneControl;
import vsiege.common.game.ZoneFlag;

public class ModeCTF extends Mode {

	public List<ZoneFlag> zones = Lists.newArrayList();
	public Map<SiegeTeam, ZoneFlag> owners = Maps.newHashMap();

	public boolean tick() {
		for (ZoneFlag zone : zones) {
			zone.tick();
		}
		for (SiegeTeam team : siege.teams()) {
			if (team.score >= this.pointsNeededToWin) {
				return true;
			}
		}
		return false;
	}

	public boolean isReady() {
		return zones.size() == siege.teams().size();
	}

	public String score(World world, Siege siege, SiegeTeam team) {
		return team.color + team.getTeamName() + ": Captured flags: " + team.score;
	}

	protected void fromNBT0(Siege siege, NBTTagCompound nbt) {
		NBTTagList czs = nbt.getTagList("VinyarionAddon_FlagZones", NBT.TAG_COMPOUND);
		zones.clear();
		owners.clear();
		for (int i = 0; i < czs.tagCount(); i++) {
			ZoneFlag zone = new ZoneFlag();
			zone.fromNBT(siege, czs.getCompoundTagAt(i));
			zones.add(zone);
			owners.put(zone.owner, zone);
		}
	}

	protected void toNBT0(Siege siege, NBTTagCompound nbt) {
		NBTTagList czs = new NBTTagList();
		for (ZoneFlag zone : zones) {
			czs.appendTag(zone.toNBT(siege, new NBTTagCompound()));
		}
		nbt.setTag("VinyarionAddon_FlagZones", czs);
	}

	public int scoringMethod(Siege siege, SiegeTeam team) {
		return team.score;
	}

	public String endMessage(Siege siege, SiegeTeam team, String message) {
		return super.endMessage(siege, team, message) + ", Flags: " + team.score;
	}

	protected String object() {
		return "flag";
	}

	public void scoreboard(List<Score> list, Scoreboard board, ScoreObjective objective, String timeRemaining, SiegeTeam team, EntityPlayerMP entityplayer, SiegePlayerData playerdata) {
		list.add(null);
		list.add(new Score(board, objective, "Flags stolen: " + team.score));
		list.add(new Score(board, objective, "Flags lost: " + team.antiscore));
	}
	
	public void printMVP(Siege siege, List<SiegeTeam> siegeTeams) {
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
			SiegeTeam mvpteam = siege.getPlayerTeam(mvpID);
			AddonHooks.messageAllSiegePlayers(siege, "MVP was " + mvp + " (" + mvpteam.color + mvpteam.getTeamName() + EnumChatFormatting.RED + ") with " + mvpKills + " kills / " + mvpDeaths + " deaths / " + mvpScore + " flag captures");
		}
		if (longestKillstreakID != null) {
			String streakPlayer = UsernameCache.getLastKnownUsername(longestKillstreakID);
			SiegeTeam ksteam = siege.getPlayerTeam(longestKillstreakID);
			AddonHooks.messageAllSiegePlayers(siege, "Longest killstreak was " + streakPlayer + " (" + ksteam.color + ksteam.getTeamName() + EnumChatFormatting.RED + ") with a killstreak of " + longestKillstreak);
		}
	}
	
	public void startSiege() {
		for(SiegeTeam team : siege.teams()) {
			team.score = team.antiscore = 0;
		}
	}

	public CommandException generateException() {
		return new CommandException("Siege %s cannot be started - it requires a location, at least one team, and a home zone for each team", siege.getSiegeName());
	}

}
