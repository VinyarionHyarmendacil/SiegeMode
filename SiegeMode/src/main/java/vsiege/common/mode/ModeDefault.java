package vsiege.common.mode;

import net.minecraft.command.CommandException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import siege.common.siege.Siege;
import siege.common.siege.SiegeTeam;
import vsiege.common.addon.AddonHooks;

public class ModeDefault extends Mode {
	
	public ModeDefault() {
		this.pointsNeededToWin = -1;
	}

	public boolean tick() {
		return false;
	}

	public String score(World world, Siege siege, SiegeTeam team) {
		return team.color + team.getTeamName() + EnumChatFormatting.GOLD + ": Kills: " + team.getTeamKills();
	}

	protected void fromNBT0(Siege siege, NBTTagCompound nbt) {
	}

	protected void toNBT0(Siege siege, NBTTagCompound nbt) {
	}

	protected String object() {
		return "kill";
	}

	public int scoringMethod(Siege siege, SiegeTeam team) {
		return team.getTeamKills();
	}

	public boolean isReady() {
		return true;
	}

	public CommandException generateException() {
		return null;
	}

}