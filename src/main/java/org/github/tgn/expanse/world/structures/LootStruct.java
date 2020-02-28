package org.github.tgn.expanse.world.structures;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.github.tgn.expanse.Expanse;
import java.util.Random;

public class LootStruct extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk source) {
		if(random.nextInt(25) == 0) {
			Block block = source.getBlock(random.nextInt(16), random.nextInt(256), random.nextInt(16));
			block.setType(Material.CHEST, false);
			PersistentDataContainer container = ((Chest) block.getState()).getPersistentDataContainer();
			container.set(new NamespacedKey(Expanse.instance, "chestKey"), PersistentDataType.LONG, random.nextLong());
		}
	}
}
