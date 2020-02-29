package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.github.tgn.expanse.entities.CustomEntity;

public class ArrowStormAttack<E extends ProjectileSource & Entity> implements Attack<E> {
	private final int times;

	public ArrowStormAttack(int times) {this.times = times;}

	@Override
	public void attack(CustomEntity<E> instance, Player player, E entity) {
		for (int i = 0; i < this.times; i++) {
			Vector vector = entity.getLocation(player.getLocation()).toVector(); // get entity -> player vector
			vector.rotateAroundX(RANDOM.nextDouble() / 2);
			vector.rotateAroundY(RANDOM.nextDouble() / 2);
			vector.rotateAroundZ(RANDOM.nextDouble() / 2);
			vector.normalize();
			entity.launchProjectile(Arrow.class, vector);
		}
	}
}
