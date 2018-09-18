package vsiege.common.rule;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import siege.common.siege.Siege;
import vsiege.common.mode.Mode;
import vsiege.common.mode.ModeCTF;
import vsiege.common.mode.ModeDefault;
import vsiege.common.mode.ModeDomination;

public abstract class Rule {

	public static String[] values() {
		return modeNames.toArray(new String[modeNames.size()]);
	}
	public static Rule of(int i) {
		return modes.get(i).get();
	}
	public static Rule of(String string) {
		return of(modeNames.indexOf(string));
	}
	private static List<String> modeNames = Lists.newArrayList(
		"lives-player", 
		"lives-team"
	);
	private static List<Class<? extends Rule>> classes = Lists.newArrayList(
		RuleLivesPlayer.class, 
		RuleLivesTeam.class
	);
	private static List<Supplier<Rule>> modes = Lists.<Supplier<Rule>>newArrayList(
		() -> {return new RuleLivesPlayer();}, 
		() -> {return new RuleLivesTeam();}
	);
	
	public void tick(Siege siege) {}
	public void playerLogin(Siege siege, EntityPlayer player) {}
	public void playerLogout(Siege siege, EntityPlayer player) {}
	public void playerJoin(Siege siege, EntityPlayer player) {}
	public void playerLeave(Siege siege, EntityPlayer player) {}
	public void playerDie(Siege siege, EntityPlayer player) {}
	public void toNBT(NBTTagCompound nbt) {}
	public void fromNBT(NBTTagCompound nbt) {}
	public void playerRespawn(Siege siege, EntityPlayer player) {}
	public void setValue(ICommandSender sender, String string) throws CommandException {}
}
