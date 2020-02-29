package org.github.tgn.expanse.items.custom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.github.tgn.expanse.items.CanInteractWith;
import org.github.tgn.expanse.items.CustomItem;

public class Arsenic implements CustomItem, CanInteractWith {
	@Override
	public void interact(PlayerInteractEvent event) {
		event.getPlayer().damage(10000, event.getPlayer()); // gotem
	}

	@Override
	public ItemStack createBaseStack() {
		ItemStack arsenic = new ItemStack(Material.GUNPOWDER);
		ItemMeta meta = arsenic.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY+"Arsenic");
		arsenic.setItemMeta(meta);
		return arsenic;
	}
}
