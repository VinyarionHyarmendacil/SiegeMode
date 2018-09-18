package vsiege.common.rule;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import siege.common.siege.Siege;
import vsiege.common.mode.Mode;

public class RuleHandler {
	private final Mode mode;
	public RuleHandler(Mode mode) {
		this.mode = mode;
	}
	public void tick(Siege siege) {
		for(Rule rule : mode.rules) rule.tick(siege);
	}
	public void playerLogin(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerLogin(siege, player);
	}
	public void playerLogout(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerLogout(siege, player);
	}
	public void playerJoin(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerJoin(siege, player);
	}
	public void playerLeave(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerLeave(siege, player);
	}
	public void playerDie(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerDie(siege, player);
	}
	public void toNBT(NBTTagCompound nbt) {
		for(Rule rule : mode.rules) rule.toNBT(nbt);
	}
	public void fromNBT(NBTTagCompound nbt) {
		for(Rule rule : mode.rules) rule.fromNBT(nbt);
	}
	public void playerRespawn(Siege siege, EntityPlayer player) {
		for(Rule rule : mode.rules) rule.playerRespawn(siege, player);
	}
}
