package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.github.tgn.expanse.entities.CustomEntity;

public class LaunchAttack implements Attack<Entity> {
	@Override
	public void attack(CustomEntity instance, Player player, Entity entity) {
		player.setVelocity(new Vector(0, 1, 0));
	}
}
