package org.github.tgn.expanse.attacks;

import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;

public class SelfDestructAttack implements Attack<CustomEntity> {
	@Override
	public void attack(CustomEntity instance, Player player, Entity entity) {
		World world = player.getWorld();
		if(entity instanceof Damageable) {
			Damageable damageable = (Damageable) entity;
			if(damageable.getHealth() > damageable.getMaxHealth()*.1) {
				return;
			}
		}
		world.createExplosion(entity.getLocation(), 6f);
		entity.remove();
	}
}
