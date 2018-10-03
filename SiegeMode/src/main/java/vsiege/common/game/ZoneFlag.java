package vsiege.common.game;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import static net.minecraft.util.EnumChatFormatting.*;
import net.minecraft.world.World;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import siege.common.siege.SiegeTeam;
import vsiege.common.addon.AddonHooks;
import vsiege.common.mode.ModeCTF;

public class ZoneFlag extends Zone {

	public ZoneFlag() {}

	public ZoneFlag(SiegeTeam team, int x, int y, int z, int size) {
		super(x, y, z, size);
		owner = team;
	}

	public SiegeTeam owner;
	public boolean hasFlag = true;

	public void tick() {
		if(siege.getTicksRemaining() % 5 != 0) return;
		for(Object o : siege.world().getEntitiesWithinAABB(EntityPlayer.class, box)) {
			EntityPlayer player = (EntityPlayer)o;
			if(siege.hasPlayer(player)) {
				if(siege.getPlayerTeam(player).getTeamName().equals(owner.getTeamName())) {
					SiegeTeam carried = getCarriedFlag(siege, player);
					if(carried != null) {
						captureFlag((ModeCTF)siege.mode, player, owner, carried);
					}
				} else if(hasFlag) {
					pickupFlag((ModeCTF)siege.mode, player);
				}
			}
		}
	}

	private void captureFlag(ModeCTF mode, EntityPlayer player, SiegeTeam winner, SiegeTeam loser) {
		AddonHooks.messageAllSiegePlayers(siege, winner.color + winner.getTeamName() + GOLD + " has captured the flag of " + loser.color + loser.getTeamName() + GOLD + "!");
		player.getEntityData().removeTag("VinyarionAddon_Flag");
		mode.owners.get(loser).hasFlag = true;
		winner.score++;
		loser.antiscore++;
		siege.getPlayerData(player).addonData.personalscore++;
		siege.markDirty();
	}

	private void pickupFlag(ModeCTF mode, EntityPlayer player) {
		SiegeTeam team = siege.getTeam(player.getEntityData().getString("VinyarionAddon_Flag"));
		player.getEntityData().setString("VinyarionAddon_Flag", owner.getTeamName());
		if(team != null) {
			mode.owners.get(team).hasFlag = true;
			AddonHooks.messageAllSiegePlayers(siege, YELLOW + player.getCommandSenderName() + GOLD + " has dropped the flag of " + team.color + team.getTeamName() + GOLD + "!");
		}
		hasFlag = false;
		AddonHooks.messageAllSiegePlayers(siege, YELLOW + player.getCommandSenderName() + GOLD + " has picked up the flag of " + owner.color + owner.getTeamName() + GOLD + "!");
		siege.markDirty();
	}

	public static void dropFlag(ModeCTF mode, Siege siege, EntityPlayer player) {
		SiegeTeam team = siege.getTeam(player.getEntityData().getString("VinyarionAddon_Flag"));
		player.getEntityData().removeTag("VinyarionAddon_Flag");
		if(team != null) {
			mode.owners.get(team).hasFlag = true;
			AddonHooks.messageAllSiegePlayers(siege, YELLOW + player.getCommandSenderName() + GOLD + " has dropped the flag of " + team.color + team.getTeamName() + GOLD + "!");
		}
		siege.markDirty();
	}

	public static SiegeTeam getCarriedFlag(Siege siege, EntityPlayer player) {
		return siege.getTeam(player.getEntityData().getString("VinyarionAddon_Flag"));
	}

	protected void fromNBT0(NBTTagCompound nbt) {
		this.owner = this.siege.getTeam(nbt.getString("VinyarionAddon_TeamName"));
		this.hasFlag = nbt.getBoolean("VinyarionAddon_HasFlag");
	}

	protected void toNBT0(NBTTagCompound nbt) {
		nbt.setString("VinyarionAddon_TeamName", this.owner.getTeamName());
		nbt.setBoolean("VinyarionAddon_HasFlag", this.hasFlag);
	}

}
