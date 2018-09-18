package vsiege.common.game;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import siege.common.siege.Siege;

public abstract class Zone {

	public Zone() {}
	
	public Zone(int x, int y, int z, int size) {
		box = AxisAlignedBB.getBoundingBox((double)(x - size) + 0.5, (double)y - 1, (double)(z - size) + 0.5, (double)(x + size) + 0.5, y + 1, (double)(z + size) + 0.5);
	}

	public AxisAlignedBB box;
	public Siege siege;
	
	public void fromNBT(Siege siege, NBTTagCompound nbt) {
		this.siege = siege;
		box = AxisAlignedBB.getBoundingBox(nbt.getDouble("minx"), nbt.getDouble("miny"), nbt.getDouble("minz"), nbt.getDouble("maxx"), nbt.getDouble("maxy"), nbt.getDouble("maxz"));
		this.fromNBT0(nbt);
	}
	
	public NBTTagCompound toNBT(Siege siege, NBTTagCompound nbt) {
		this.siege = siege;
		nbt.setDouble("minx", box.minX);
		nbt.setDouble("miny", box.minY);
		nbt.setDouble("minz", box.minY);
		nbt.setDouble("maxx", box.maxX);
		nbt.setDouble("maxy", box.maxY);
		nbt.setDouble("maxz", box.maxZ);
		this.toNBT0(nbt);
		return nbt;
	}
	
	protected abstract void fromNBT0(NBTTagCompound nbt);
	protected abstract void toNBT0(NBTTagCompound nbt);

	public abstract void tick();
	
}
