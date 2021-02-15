package vsiege.common.addon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class AddonTeleporter extends Teleporter {

	private WorldServer world;

	public AddonTeleporter(WorldServer worldserver) {
		super(worldserver);
		this.world = worldserver;
	}

	public void placeInPortal(Entity entity, double prevX, double prevY, double prevZ, float f) {
		int y = getTrueTopBlock(entity.worldObj, 0, 0);
		entity.setLocationAndAngles(0.5, y + 1.0, 0.5, entity.rotationYaw, 0.0f);
	}

	public static int getTrueTopBlock(World world, int i, int k) {
		final Chunk chunk = world.getChunkProvider().provideChunk(i >> 4, k >> 4);
		for(int j = chunk.getTopFilledSegment() + 15; j > 0; --j) {
			final Block block = world.getBlock(i, j, k);
			if(block.getMaterial().blocksMovement() && block.getMaterial() != Material.leaves && !block.isFoliage(world, i, j, k)) {
				return j + 1;
			}
		}
		return -1;
	}

}
