package org.github.tgn.expanse.items;

import org.bukkit.inventory.ItemStack;
import java.util.Random;

public interface CustomItem {
	Random RANDOM = new Random();
	/**
	 * do <b>NOT</b> call this, use a {@link CustomItemFactory} instead!
	 * @return a base item
	 */
	ItemStack createBaseStack();
}
