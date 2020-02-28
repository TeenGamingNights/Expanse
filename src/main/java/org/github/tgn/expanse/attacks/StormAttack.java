package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.projectiles.ProjectileSource;
import org.github.tgn.expanse.entities.CustomEntity;

public class StormAttack implements Attack<CustomEntity> {
	@Override
	public void attack(CustomEntity instance, Player player, Entity entity) {
		if(entity instanceof ProjectileSource) {
			((ProjectileSource) entity).launchProjectile()
		}
	}
}
