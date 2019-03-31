package vsiege.common.addon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import siege.common.kit.Kit;
import siege.common.siege.BackupSpawnPoint;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
import vsiege.common.game.ZoneFlag;
import vsiege.common.mode.ModeCTF;

public class AddonHooks {

	public static ThreadLocal<SiegePlayerData> lastLeft = ThreadLocal.withInitial(()->null);

	public static void playerJoinsSiege(EntityPlayer player, Siege siege, SiegeTeam team, Kit kit) {
		if(siege == null) return;
		AddonPlayerData pd = siege.getPlayerData(player).addonData;
		pd.isSiegeActive = true;
		pd.joinedSiegePos = new double[]{player.posX, player.posY, player.posZ};
		pd.joinedSiegeDim = siege.world().provider.dimensionId;
		siege.mode.ruleHandler.playerJoin(siege, player);
	}

	public static void playerLeavesSiege(EntityPlayerMP player, Siege siege, SiegeTeam team) {
		if(siege == null) return;
		AddonPlayerData pd = siege.getPlayerData(player).addonData;
		lastLeft.set(siege.getPlayerData(player));
		siege.mode.ruleHandler.playerLeave(siege, player);
		pd.isSiegeActive = false;
	}

	public static void playerLogsInActive(EntityPlayer player, Siege siege) {
		if(siege == null) return;
		siege.mode.ruleHandler.playerLogin(siege, player);
	}

	public static void playerLogsInInactive(EntityPlayer player, Siege siege, SiegePlayerData old) {
		if(siege == null) return;
		if(!siege.isActive() || siege.isDeleted()) {
			old.updateSiegeScoreboard((EntityPlayerMP)player, false);
			lastLeft.set(old);
			BackupSpawnPoint bsp = old.getBackupSpawnPoint();
			if (bsp != null) {
				player.setSpawnChunk(bsp.spawnCoords, bsp.spawnForced, bsp.dimension);
			}
			Kit.clearPlayerInvAndKit(player);
			if (siege.getDispelEnd()) {
				Siege.dispel(player);
			}
		}
	}

	public static void playerLogsOut(EntityPlayer player, Siege siege) {
		if(siege == null) return;
		siege.mode.ruleHandler.playerLogout(siege, player);
	}

	private static final Method masp = ReflectionHelper.findMethod(Siege.class, null, new String[]{"messageAllSiegePlayers"}, String.class);
	public static void messageAllSiegePlayers(Siege siege, String text) {
		if(siege == null) return;
		try {
			masp.invoke(siege, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playerDies(EntityPlayer player, Siege siege, SiegeTeam team) {
		if(siege == null) return;
		if(siege.mode instanceof ModeCTF) {
			ZoneFlag.dropFlag((ModeCTF)siege.mode, siege, player);
		}
		siege.mode.ruleHandler.playerDie(siege, player);
	}

	public static void playerRespawns(EntityPlayer player, Siege siege, SiegeTeam team) {
		if(siege == null) return;
		siege.mode.ruleHandler.playerRespawn(siege, player);
	}

}
