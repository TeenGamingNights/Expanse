package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.entities.CustomEntity;
import java.util.Random;

public interface Attack<E extends Entity> {
	Random RANDOM = CustomEntity.RANDOM;
	void attack(CustomEntity<E> instance, Player player, E entity);
}
