package vsiege.common.addon;

import net.minecraft.nbt.NBTTagCompound;
import siege.common.siege.Siege;
import siege.common.siege.SiegePlayerData;

public class AddonPlayerData {
	
	private final SiegePlayerData parent;
	public final Siege siege;
	public boolean isSiegeActive = false;
	public double[] joinedSiegePos = new double[3];
	public int joinedSiegeDim = 0;
	public int personalscore = 0;
	
	public AddonPlayerData(SiegePlayerData parent, Siege siege) {
		this.parent = parent;
		this.siege = siege;
	}
	
	public void toNBT(NBTTagCompound nbt) {
		nbt.setBoolean("VinyarionAddon_IsSiegeActive", this.isSiegeActive);
		nbt.setDouble("VinyarionAddon_JoinedSiegePosX", this.joinedSiegePos[0]);
		nbt.setDouble("VinyarionAddon_JoinedSiegePosY", this.joinedSiegePos[1]);
		nbt.setDouble("VinyarionAddon_JoinedSiegePosZ", this.joinedSiegePos[2]);
		nbt.setInteger("VinyarionAddon_JoinedSiegePosDim", this.joinedSiegeDim);
		nbt.setInteger("VinyarionAddon_PersonalScore", this.personalscore);
	}
	
	public void fromNBT(NBTTagCompound nbt) {
		this.isSiegeActive = nbt.getBoolean("VinyarionAddon_IsSiegeActive");
		this.joinedSiegePos = new double[]{
			nbt.getDouble("VinyarionAddon_JoinedSiegePosX"), 
			nbt.getDouble("VinyarionAddon_JoinedSiegePosY"), 
			nbt.getDouble("VinyarionAddon_JoinedSiegePosZ")
		};
		this.joinedSiegeDim = nbt.getInteger("VinyarionAddon_JoinedSiegePosDim");
		this.personalscore = nbt.getInteger("VinyarionAddon_PersonalScore");
	}
	
}
