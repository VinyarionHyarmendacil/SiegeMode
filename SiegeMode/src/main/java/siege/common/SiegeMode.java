package siege.common;

import java.io.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import siege.common.kit.*;
import siege.common.siege.*;
import vsiege.common.addon.CommandSiegeZone;
import vsiege.common.addon.CommandSiegeList;
import vsiege.common.addon.CommandSiegeMode;
import vsiege.common.addon.CommandSiegeRule;
import vsiege.common.addon.CommandSiegeTeamColor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "siegemode", version = "V.1.7", acceptableRemoteVersions = "*")
public class SiegeMode
{
	// TODO : modid
	@Mod.Instance("siegemode")
	public static SiegeMode instance;
	
	private EventHandler eventHandler;
	
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		eventHandler = new EventHandler();
		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		KitDatabase.reloadAll();
		SiegeDatabase.reloadAll();
		event.registerServerCommand(new CommandKit());
		event.registerServerCommand(new CommandSiegeSetup());
		event.registerServerCommand(new CommandSiegePlay());
		// TODO : Vinyarion's Addon start
		event.registerServerCommand(new CommandSiegeMode());
		event.registerServerCommand(new CommandSiegeZone());
		event.registerServerCommand(new CommandSiegeList());
		event.registerServerCommand(new CommandSiegeRule());
		event.registerServerCommand(new CommandSiegeTeamColor());
		// Addon end
	}
	
	public static File getSiegeRootDirectory()
	{
		return new File(DimensionManager.getCurrentSaveRootDirectory(), "siegemode");
	}
	
	public static NBTTagCompound loadNBTFromFile(File file) throws FileNotFoundException, IOException
	{
		if (file.exists())
		{
			return CompressedStreamTools.readCompressed(new FileInputStream(file));
		}
		else
		{
			return new NBTTagCompound();
		}
	}
	
	public static void saveNBTToFile(File file, NBTTagCompound nbt) throws FileNotFoundException, IOException
	{
		CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file));
	}
}
