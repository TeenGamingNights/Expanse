package org.github.tgn.expanse.world.structures;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.generator.BlockPopulator;
import org.github.tgn.expanse.Expanse;
import org.github.tgn.expanse.attacks.Teleport;
import org.github.tgn.expanse.entities.CustomEntity;
import java.util.Random;

import static org.github.tgn.expanse.Expanse.ENTITY_MANAGER;

public class LootStruct extends BlockPopulator {
	public static final Random RANDOM = new Random();
	@Override
	public void populate(World world, Random random, Chunk source) {
		if(random.nextInt(50) == 0) {
			Location location = new Location(world, source.getX()*16, random.nextInt(64)+64, source.getZ()*16);
			Teleport.makePlatform(location.clone().subtract(0, 1, 0), 5);
			Expanse.Pair<CustomEntity, Entity> entityPair = Expanse.SPAWN.get(RANDOM.nextInt(Expanse.SPAWN.size())).apply(location);
			ENTITY_MANAGER.attach(entityPair.b, entityPair.a);
		}
	}
}
