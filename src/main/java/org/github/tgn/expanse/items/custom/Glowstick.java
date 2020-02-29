package org.github.tgn.expanse.items.custom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.github.tgn.expanse.items.CanInteractWith;
import org.github.tgn.expanse.items.CustomItem;
import java.util.Collections;
import java.util.Random;

public class Glowstick implements CustomItem, CanInteractWith {
	@Override
	public void interact(PlayerInteractEvent event) {
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 1));
	}

	@Override
	public ItemStack createBaseStack() {
		ItemStack glowball = new ItemStack(Material.BLAZE_ROD);
		glowball.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		ItemMeta itemMeta = glowball.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW+"Glow stick!");
		itemMeta.setLore(Collections.singletonList(ChatColor.YELLOW+"Makes you glow!"));
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();
		// make it unstackable
		container.set(NamespacedKey.randomKey(), PersistentDataType.LONG, new Random().nextLong());
		glowball.setItemMeta(itemMeta);
		return glowball;
	}
}
