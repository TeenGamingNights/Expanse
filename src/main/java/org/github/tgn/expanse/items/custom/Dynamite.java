package org.github.tgn.expanse.items.custom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.github.tgn.expanse.Expanse;
import org.github.tgn.expanse.items.CanInteractWith;
import org.github.tgn.expanse.items.CustomItem;

public class Dynamite implements CustomItem, CanInteractWith {
	@SuppressWarnings ("ConstantConditions")
	@Override
	public void interact(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (block != null) {
			event.getItem().setAmount(event.getItem().getAmount() - 1);
			Bukkit.getScheduler().runTaskLater(Expanse.instance, () -> block.getWorld().createExplosion(block.getLocation(), 3), 40);
		}
	}

	@Override
	public ItemStack createBaseStack() {
		ItemStack dynamite = new ItemStack(Material.BLAZE_ROD);
		ItemMeta itemMeta = dynamite.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RED+"Dynamite");
		dynamite.setItemMeta(itemMeta);
		dynamite.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		return dynamite;
	}
}
