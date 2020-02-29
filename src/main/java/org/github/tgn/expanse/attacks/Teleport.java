package org.github.tgn.expanse.attacks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;

public class Teleport implements Attack<Entity> {
	private final int bounds;

	public Teleport(int bounds) {this.bounds = bounds;}

	@Override
	public void attack(CustomEntity<Entity> instance, Player player, Entity entity) {
		Location current = entity.getLocation();
		current.add(RANDOM.nextInt() % this.bounds, RANDOM.nextInt() % this.bounds, RANDOM.nextInt() % this.bounds);
		entity.teleport(current);
		makePlatform(current.subtract(0, 1, 0).clone());
	}

	private static void makePlatform(Location location) {
		int platformSize = 2;
		for (int x = -platformSize; x < platformSize+1; x++) {
			for (int z = -platformSize; z < platformSize+1; z++) {
				Location platformLocation = location.clone();
				platformLocation.add(x, 0, z);
				Block block = platformLocation.getBlock();
				if(!block.getType().isSolid()) {
					block.breakNaturally();
					block.setType(Material.STONE);
				}
			}
		}
	}
}
