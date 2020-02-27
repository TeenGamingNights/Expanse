package org.github.tgn.expanse.world.population;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import java.util.Random;

public class ChestPopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk source) {
		Block block = source.getBlock(random.nextInt(16), random.nextInt(256), random.nextInt(16));
		block.setType(Material.STONE);
	}
}
