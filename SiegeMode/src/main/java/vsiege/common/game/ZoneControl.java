package vsiege.common.game;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import static net.minecraft.util.EnumChatFormatting.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
import vsiege.common.addon.AddonHooks;

public class ZoneControl extends Zone {

	public ZoneControl() {}

	public ZoneControl(String name, int x, int y, int z, int size) {
		super(x, y, z, size);
		this.name = name;
	}

	public String name;
	public SiegeTeam occupiers = null;
	public SiegeTeam attackers = null;
	public int ticksHeld = 0;
	public int ticksTillOccupation = 120 / 5;

	protected void fromNBT0(NBTTagCompound nbt) {
		name = nbt.getString("name");
		occupiers = this.siege.getTeam(nbt.getString("occupiers"));
		attackers = this.siege.getTeam(nbt.getString("attackers"));
		ticksHeld = nbt.getInteger("ticksHeld");
	}

	protected void toNBT0(NBTTagCompound nbt) {
		nbt.setString("name", name);
		nbt.setString("occupiers", occupiers == null ? "" : (occupiers.color + occupiers.getTeamName()));
		nbt.setString("attackers", attackers == null ? "" : (attackers.color + attackers.getTeamName()));
		nbt.setInteger("ticksHeld", ticksHeld);
	}

	public void tick() {
		if(siege.getTicksRemaining() % 5 != 0) return;
		int[] teams = new int[siege.listTeamNames().size()];
		for(Object o : siege.world().getEntitiesWithinAABB(EntityPlayer.class, box)) {
			EntityPlayer player = (EntityPlayer)o;
			if(siege.hasPlayer(player)) {
				teams[siege.listTeamNames().indexOf(siege.getPlayerTeam(player).getTeamName())]++;
			}
		}
		int high = 0;
		for(int i = 1; i < teams.length; i++) {
			if(teams[high] < teams[i]) {
				high = i;
			}
		}
		List<SiegeTeam> highs = Lists.newArrayList();
		for(int i = 0; i < teams.length; i++) {
			if(teams[high] == teams[i]) {
				highs.add(siege.getTeam(siege.listTeamNames().get(i)));
			}
		}
		if(teams[high] == 0) return;
		if(highs.size() == 1) {
			if(highs.get(0) == occupiers) {
				ticksHeld = 0;
				attackers = null;
			} else if(highs.get(0) == attackers) {
				ticksHeld++;
				boolean captured = false;
				if(ticksHeld >= ticksTillOccupation) {
					occupied(attackers);
					captured = true;
				}
				for(Object o : siege.world().getEntitiesWithinAABB(EntityPlayer.class, box)) {
					EntityPlayer player = (EntityPlayer)o;
					if(siege.getPlayerTeam(player) == attackers) {
						siege.getPlayerData(player).addonData.personalscore += (captured ? 20 : 1);
					}
				}
			} else {
				ticksHeld = 0;
				attacked(highs.get(0));
			}
		} else if(highs.contains(occupiers) && highs.contains(attackers)) {
			ticksHeld = 0;
		}
		siege.markDirty();
	}

	private void attacked(SiegeTeam team) {
		siege.announceToAllPlayers(team.color + team.getTeamName() + GOLD + " is attacking " + WHITE + name + GOLD + "!");
		attackers = team;
	}

	private void occupied(SiegeTeam team) {
		siege.announceToAllPlayers(team.color + team.getTeamName() + GOLD + " has taken " + WHITE + name + GOLD + "!");
		occupiers = team;
		attackers = null;
	}

	public void setValue(ICommandSender sender, String string) throws CommandException {
		ticksTillOccupation = CommandBase.parseIntWithMin(sender, string, 0) * 4;
	}

}
