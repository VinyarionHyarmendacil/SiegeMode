package vsiege.common.mode;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
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
		return team.getTeamName() + ": Score: " + team.score;
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
		return message + ", Points: " + team.score;
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
