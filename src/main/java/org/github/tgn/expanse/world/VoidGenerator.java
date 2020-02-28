package org.github.tgn.expanse.world;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.github.tgn.expanse.world.structures.LootStruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {
	private static final int SIZE = 16;
	private final Biome[] biomes;
	public VoidGenerator(Biome[] biomes) {
		this.biomes = biomes;
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid grid) {
		ChunkData data = this.createChunkData(world);
		this.populateBiomes(grid, world.getSeed(), chunkX, chunkZ);
		return data;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> populators = new ArrayList<>();
		populators.add(new LootStruct());
		return populators;
	}

	/**
	 * populate the biomes in the grid given the seed
	 * @param grid the biome grid
	 * @param seed the world seed at the location
	 * @param chunkX the chunk's X coordinate
	 * @param chunkZ the chunk's Y coordinate
	 */
	private void populateBiomes(BiomeGrid grid, long seed, int chunkX, int chunkZ) {
		VornoiZoom zoom = new VornoiZoom(seed, 16); // biome generator
		for (int rx = 0; rx < SIZE; rx++) { // relative x
			for (int rz = 0; rz < SIZE; rz++) { // relative z
				int bx = rx + chunkX * 16; // block x
				int bz = rz + chunkZ * 16; // block z
				Biome biome = this.biomes[zoom.zoom(bx, bz) % this.biomes.length];
				grid.setBiome(rx, rz, biome);
			}
		}
	}
}
