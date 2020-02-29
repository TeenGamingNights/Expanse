package org.github.tgn.expanse.attacks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.github.tgn.expanse.entities.CustomEntity;

public class FreezeAttack implements Attack<Entity> {
	@Override
	public void attack(CustomEntity<Entity> instance, Player player, Entity entity) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
	}
}
