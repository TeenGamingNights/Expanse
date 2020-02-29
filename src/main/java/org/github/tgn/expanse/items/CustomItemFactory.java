package org.github.tgn.expanse.items;

import net.devtech.yajslib.persistent.PersistentRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.github.tgn.expanse.Expanse;
import java.io.IOException;

public class CustomItemFactory {
	private CustomItemFactory() {}

	public static ItemStack createNew(PersistentRegistry registry, Class<? extends CustomItem> type) {
		return wrap(registry, registry.blank(type));
	}

	public static ItemStack wrap(PersistentRegistry registry, CustomItem obj) {
		try {
			ItemStack stack = obj.createBaseStack();
			ItemMeta meta = stack.getItemMeta();
			if(meta == null)
				meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
			PersistentDataContainer container = meta.getPersistentDataContainer();
			container.set(new NamespacedKey(Expanse.instance, "asyncore.object_data"), PersistentDataType.BYTE_ARRAY, registry.toByteArray(obj));
			stack.setItemMeta(meta);
			return stack;
		} catch (IOException e) {
			throw new RuntimeException("Fatal error in creating new item stack with " + obj, e);
		}
	}

	public static CustomItem from(PersistentRegistry registry, ItemStack stack) {
		try {
			ItemMeta meta = stack.getItemMeta();
			if(meta == null)
				return null;
			PersistentDataContainer container = meta.getPersistentDataContainer();
			byte[] data = container.get(new NamespacedKey(Expanse.instance, "asyncore.object_data"), PersistentDataType.BYTE_ARRAY);
			if (data != null)
				return (CustomItem) registry.fromByteArray(data);
			else
				return null;
		} catch (IOException e) {
			throw new RuntimeException("Fatal error in parsing custom object!", e);
		}
	}

	public static void wrap(PersistentRegistry registry, ItemStack stack, CustomItem item) {
		try {
			ItemMeta meta = stack.getItemMeta();
			if(meta == null)
				meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
			if(meta == null) {
				System.out.println("nope");
				return;
			}
			PersistentDataContainer container = meta.getPersistentDataContainer();
			container.set(new NamespacedKey(Expanse.instance, "asyncore.object_data"), PersistentDataType.BYTE_ARRAY, registry.toByteArray(item));
			stack.setItemMeta(meta);
		} catch (IOException e) {
			throw new RuntimeException("Fatal error in creating new item stack with " + item, e);
		}
	}
}
