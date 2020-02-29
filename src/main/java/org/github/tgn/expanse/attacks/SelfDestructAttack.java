package org.github.tgn.expanse.attacks;

import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;

public class SelfDestructAttack implements Attack<Damageable> {
	@Override
	public void attack(CustomEntity<Damageable> instance, Player player, Damageable entity) {
		World world = player.getWorld();
		if (entity.getHealth() > entity.getMaxHealth() * .1) {
			return;
		}
		world.createExplosion(entity.getLocation(), 6f);
		entity.remove();
	}
}
