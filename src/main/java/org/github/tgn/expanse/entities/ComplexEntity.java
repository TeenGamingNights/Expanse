package org.github.tgn.expanse.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.attacks.Attack;
import org.github.tgn.expanse.util.WeightedList;

public abstract class ComplexEntity<E extends Entity> implements CustomEntity<E> {
	protected final WeightedList<Attack<?>> attacks = new WeightedList<>();
	@Override
	public void attack(Player player, E current) {
		// ikr, insane complex
		Attack<E> attack = (Attack<E>) this.attacks.get(RANDOM.nextInt(this.attacks.size()));
		System.out.println(attack);
		attack.attack(this, player, current);
	}
}
