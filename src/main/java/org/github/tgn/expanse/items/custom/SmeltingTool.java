package org.github.tgn.expanse.items.custom;

import net.devtech.yajslib.annotations.Reader;
import net.devtech.yajslib.annotations.Writer;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
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
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class SmeltingTool implements CustomItem, CanBreakWith {
	private static final Material[] MATERIALS = Material.values();
	private Material type;

	public SmeltingTool(Material type) {this.type = type;}

	@Override
	public ItemStack createBaseStack() {
		Material toolType = this.type;
		ItemStack pick = new ItemStack(toolType);
		ItemMeta meta = pick.getItemMeta();
		String name = toolType.name();
		meta.setDisplayName(ChatColor.GOLD+"Magma " + name.substring(name.lastIndexOf('_')+1).toLowerCase());
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
				boolean input = ((CookingRecipe<?>) recipe).getInputChoice().test(stack);
				if(input) {
					ItemStack in = recipe.getResult().clone();
					in.setAmount(stack.getAmount());
					return in;
				}
			}
		}

		return stack;
	}

	@Reader(439209120920L)
	public void read(PersistentInput input) throws IOException {
		this.type = MATERIALS[input.readInt()];
	}

	@Writer(439209120920L)
	public void write(PersistentOutput output) throws IOException {
		output.writeInt(this.type.ordinal());
	}

}
