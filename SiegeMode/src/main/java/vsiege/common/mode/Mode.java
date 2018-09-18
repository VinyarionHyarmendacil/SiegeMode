package vsiege.common.mode;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import siege.common.siege.Siege;
import siege.common.siege.SiegeDatabase;
import siege.common.siege.SiegePlayerData;
import siege.common.siege.SiegeTeam;
import vsiege.common.rule.Rule;
import vsiege.common.rule.RuleHandler;

public abstract class Mode {

	public static String[] values() {
		return modeNames.toArray(new String[modeNames.size()]);
	}
	public static Mode of(int i) {
		return modes.get(i).get();
	}
	public static Mode of(String string) {
		return of(modeNames.indexOf(string));
	}
	private static List<String> modeNames = Lists.newArrayList(
		"deathmatch", 
		"domination", 
		"ctf"
	);
	private static List<Class<? extends Mode>> classes = Lists.newArrayList(
		ModeDefault.class, 
		ModeDomination.class, 
		ModeCTF.class
	);
	private static List<Supplier<Mode>> modes = Lists.<Supplier<Mode>>newArrayList(
		() -> {return new ModeDefault();}, 
		() -> {return new ModeDomination();}, 
		() -> {return new ModeCTF();}
	);

	public int pointsNeededToWin = 0;
	protected Siege siege;

	public Mode setSiege(Siege siege) {
		this.siege = siege;
		return siege.mode = this;
	}

	public List<Rule> rules = Lists.newArrayList();
	public final RuleHandler ruleHandler = new RuleHandler(this);
	
	protected abstract String object();
	
	public abstract boolean tick();
	
	public abstract boolean isReady();
	
	public abstract String score(World world, Siege siege, SiegeTeam team);
	
	public final void fromNBT(Siege siege, NBTTagCompound nbt) {
		this.fromNBT0(siege, nbt);
		this.ruleHandler.fromNBT(nbt.getCompoundTag("VinyarionAddon_Rules"));
		this.pointsNeededToWin = nbt.getInteger("VinyarionAddon_PointsNeededToWin");
	}
	
	public final void toNBT(Siege siege, NBTTagCompound nbt) {
		this.toNBT0(siege, nbt);
		if(!nbt.hasKey("VinyarionAddon_Rules", NBT.TAG_COMPOUND)) nbt.setTag("VinyarionAddon_Rules", new NBTTagCompound());
		this.ruleHandler.toNBT(nbt.getCompoundTag("VinyarionAddon_Rules"));
		nbt.setInteger("VinyarionAddon_PointsNeededToWin", this.pointsNeededToWin);
	}

	protected abstract void fromNBT0(Siege siege, NBTTagCompound nbt);
	
	protected abstract void toNBT0(Siege siege, NBTTagCompound nbt);
	
	public abstract int scoringMethod(Siege siege, SiegeTeam team);
	
	public void preEndSiege() {}
	
	public String endMessage(Siege siege, SiegeTeam team, String message) {
		return message;
	}
	
	public String object(Siege siege, boolean plural) {
		return plural ? object() + "s" : object();
	}
	
	public int ordinal() {
		return classes.indexOf(this.getClass());
	}
	
	public void scoreboard(List<Score> list, Scoreboard board, ScoreObjective objective, String timeRemaining, SiegeTeam team, EntityPlayerMP entityplayer, SiegePlayerData playerdata) {}
	
	public void startSiege() {}
	public abstract CommandException generateException();
	
}
