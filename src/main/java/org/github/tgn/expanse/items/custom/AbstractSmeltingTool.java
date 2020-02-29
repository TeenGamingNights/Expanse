package org.github.tgn.expanse.items.custom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.github.tgn.expanse.items.CanBreakWith;
import org.github.tgn.expanse.items.CustomItem;
import java.util.Collections;
import java.util.Iterator;

public abstract class AbstractSmeltingTool implements CustomItem, CanBreakWith {
	@Override
	public ItemStack createBaseStack() {
		Material toolType = this.getTool();
		ItemStack pick = new ItemStack(toolType);
		ItemMeta meta = pick.getItemMeta();
		String name = toolType.name();
		meta.setDisplayName(ChatColor.GOLD+"Magma " + name.substring(name.lastIndexOf('_')));
		meta.setLore(Collections.singletonList(ChatColor.GOLD + "Smelts the items it mines!"));
		meta.addEnchant(Enchantment.DURABILITY, 5, true);
		pick.setItemMeta(meta);
		return pick;
	}

	@SuppressWarnings ("ConstantConditions")
	@Override
	public void destroy(BlockBreakEvent event) {
		Block block = event.getBlock();
		ItemStack holding = event.getPlayer().getInventory().getItemInMainHand();
		for (ItemStack drop : holding != null ? block.getDrops(holding) : block.getDrops()) {
			ItemStack smelted = smelt(drop);
			event.setDropItems(false);
			block.getWorld().dropItemNaturally(block.getLocation(), smelted);
		}
	}

	public static ItemStack smelt(ItemStack stack) {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();

		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			if(recipe instanceof CookingRecipe) {
				ItemStack input = ((CookingRecipe<?>) recipe).getInput();
				if(input.isSimilar(stack)) {
					ItemStack in = input.clone();
					in.setAmount(stack.getAmount());
					return in;
				}
			}
		}

		return stack;
	}

	protected abstract Material getTool();
}
