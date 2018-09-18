package vsiege.common.rule;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import siege.common.siege.Siege;

public abstract class RuleLives extends Rule {

	public abstract void playerDie(Siege siege, EntityPlayer player);
	public abstract void playerJoin(Siege siege, EntityPlayer player);

	public int lives;
	protected String type;

	public void toNBT(NBTTagCompound nbt) {
		super.toNBT(nbt);
		nbt.setInteger(type, lives);
	}

	public void fromNBT(NBTTagCompound nbt) {
		super.fromNBT(nbt);
		lives = nbt.getInteger(type);
	}
	
	public void setValue(ICommandSender sender, String string) throws CommandException {
		this.lives = CommandBase.parseIntWithMin(sender, string, 0);
	}

}
