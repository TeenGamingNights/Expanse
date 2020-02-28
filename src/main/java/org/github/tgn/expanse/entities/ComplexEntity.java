package org.github.tgn.expanse.entities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.tgn.expanse.attacks.Attack;
import org.github.tgn.expanse.util.WeightedList;

public class ComplexEntity implements CustomEntity {
	protected final WeightedList<Attack> attacks = new WeightedList<>();
	private static final String[] MESSAGES = {"You will never defeat me!", "Weak!", "Die!", "So vulnerable...", "Must suck to be so weak."};

	@Override
	public void attack(Player player, Entity current) {
		if(RANDOM.nextInt(25) == 0) {
			player.sendMessage(ChatColor.RED + MESSAGES[RANDOM.nextInt(MESSAGES.length)]);
		}

		Attack attack = this.attacks.get(RANDOM.nextInt(this.attacks.size()));
		attack.attack(this, player, current);
	}
}
