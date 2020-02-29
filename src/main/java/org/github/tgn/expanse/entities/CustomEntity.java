package org.github.tgn.expanse.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Random;

public interface CustomEntity<E extends Entity> {
	Random RANDOM = new Random();
	default void attach(E entity) {}
	default void attack(Player player, E current) {}
	default void damage(EntityDamageEvent event) {}
}
