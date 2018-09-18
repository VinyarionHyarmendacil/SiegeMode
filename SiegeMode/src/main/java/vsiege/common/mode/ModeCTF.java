package vsiege.common.mode;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import siege.common.kit.KitDatabase;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
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
		return team.getTeamName() + ": Captured flags: " + team.score;
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

	protected String object() {
		return "flag";
	}

	public void scoreboard(List<Score> list, Scoreboard board, ScoreObjective objective, String timeRemaining, SiegeTeam team, EntityPlayerMP entityplayer, SiegePlayerData playerdata) {
		list.add(null);
		list.add(new Score(board, objective, "Flags stolen: " + team.score));
		list.add(new Score(board, objective, "Flags lost: " + team.antiscore));
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
