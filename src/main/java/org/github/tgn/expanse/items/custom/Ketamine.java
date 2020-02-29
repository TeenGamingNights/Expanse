package org.github.tgn.expanse.items.custom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.github.tgn.expanse.items.CanInteractWith;
import org.github.tgn.expanse.items.CustomItem;
import java.util.Arrays;
import java.util.Random;

public class Ketamine implements CustomItem, CanInteractWith {
	@Override
	public void interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 3));
	}

	@Override
	public ItemStack createBaseStack() {
		ItemStack ketamine = new ItemStack(Material.STICK);
		ItemMeta meta = ketamine.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE+"Ketamine");
		meta.setLore(Arrays.asList(ChatColor.GRAY+"Stick, needle, same thing."));
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(NamespacedKey.randomKey(), PersistentDataType.LONG, new Random().nextLong());
		ketamine.setItemMeta(meta);
		ketamine.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
		return ketamine;
	}
}
