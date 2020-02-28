package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;

public interface Attack<E extends CustomEntity> {
	void attack(E instance, Player player, Entity entity);
}
