package org.github.tgn.expanse.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;

public class SmiteAttack implements Attack<Entity> {
	private final int bolts;
	private final int range;

	public SmiteAttack(int bolts, int range) {
		this.bolts = bolts;
		this.range = range;
	}

	public SmiteAttack(int range) {
		this(range*range/10, range);
	}

	@Override
	public void attack(CustomEntity instance, Player player, Entity entity) {
		for (int i = 0; i < this.bolts; i++) {
			Location location = entity.getLocation();
			location.add(RANDOM.nextInt() % this.range, 0, RANDOM.nextInt() % this.range);
			entity.getWorld().strikeLightning(location);
		}
	}
}
