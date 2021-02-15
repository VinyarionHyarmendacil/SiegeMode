package vsiege.common.rule;

import java.util.Arrays;
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
		return Arrays.stream(RuleType.values()).map(type -> type.cmdName).toArray(String[]::new);
	}
	public static Rule of(int i) {
		return RuleType.values()[i].constructor.get();
	}
	public static Rule of(String string) {
		return RuleType.valueOf(string.replace('-', '_').toUpperCase()).constructor.get();
	}

	public static enum RuleType {
		LIVES_PLAYER(RuleLivesPlayer::new),
		LIVES_TEAM(RuleLivesTeam::new),
		;
		private RuleType(Supplier<? extends Rule> constructor) {
			this.constructor = constructor;
			this.cmdName = name().toLowerCase().replace('_', '-');
		}
		public final Supplier<? extends Rule> constructor;
		public final String cmdName;
	}
	
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
