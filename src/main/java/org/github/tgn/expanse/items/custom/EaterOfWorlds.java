package org.github.tgn.expanse.items.custom;

import net.devtech.yajslib.annotations.Reader;
import net.devtech.yajslib.annotations.Writer;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.github.tgn.expanse.items.CanInteractWith;
import org.github.tgn.expanse.items.CustomItem;
import java.io.IOException;
import java.util.*;

public class EaterOfWorlds implements CustomItem, CanInteractWith {
	private final static Set<Material> STONES = new HashSet<>();
	private static final Set<Material> PICKS = new HashSet<>();

	static {
		for (Material value : Material.values()) {
			String name = value.name();
			if (name.endsWith("_ORE")) STONES.add(value);
			else if (name.endsWith("_PICKAXE")) PICKS.add(value);
		}

		STONES.add(Material.STONE);
		STONES.add(Material.GRANITE);
		STONES.add(Material.DIORITE);
		STONES.add(Material.ANDESITE);
	}

	private static final Mode[] VALUES = Mode.values();
	private Location start;
	private Location end;
	private Location current;
	private Mode mode = Mode.ONE_DURABILITY_OFFHAND;

	@Override
	public ItemStack createBaseStack() {
		ItemStack base = new ItemStack(Material.MAGMA_CREAM);
		base.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		ItemMeta itemMeta = base.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "The weapon of choice of a true miner.");
		lore.add(ChatColor.GRAY + "Stone beware..");
		lore.add(ChatColor.WHITE + "Shift left click to change modes");
		lore.add(ChatColor.WHITE + "left click to clear positions");
		lore.add(ChatColor.WHITE + "right click to set position");
		lore.add(ChatColor.WHITE + "shift right click to dig");
		itemMeta.setDisplayName(ChatColor.GOLD + "Eater of worlds");
		PersistentDataContainer container = itemMeta.getPersistentDataContainer();
		// make it unstackable
		container.set(NamespacedKey.randomKey(), PersistentDataType.LONG, new Random().nextLong());
		itemMeta.setLore(lore);
		base.setItemMeta(itemMeta);
		return base;
	}


	@Override
	public void interact(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) { // switch mode / clear
			if (player.isSneaking()) { // clear area
				this.start = null;
				this.end = null;
				player.sendMessage(ChatColor.RED + "Positions reset!");
			} else { // change mode
				this.mode = VALUES[(this.mode.ordinal() + 1) % VALUES.length];
				player.sendMessage(ChatColor.RED + "Mode: " + this.mode.name().toLowerCase());
			}
		} else {
			if (player.isSneaking()) { // dig
				if (this.start != null && this.end != null && Objects.equals(this.start.getWorld(), this.end.getWorld())) { // verify dimension
					BoundingBox box = new BoundingBox(this.start.getX(), this.start.getY(), this.start.getZ(), this.end.getX(), this.end.getY(), this.end.getZ());
					Location newPos;
					Location temp;
					if (box.contains((temp = this.current.clone().add(0, 0, 1)).toVector())) {
						// go down z
						newPos = temp;
					} else if (box.contains((temp = new Location(this.current.getWorld(), this.current.getX() + 1 /*shuttle to edge*/, this.current.getY(), box.getMinZ())).toVector())) {
						// go down x
						newPos = temp;
					} else {
						// go down y
						newPos = new Location(this.current.getWorld(), box.getMinX(), this.current.getY() - 1, box.getMinX());
					}

					this.current = newPos; // update
					ItemStack pickaxe = null;
					PlayerInventory inventory = player.getInventory();
					switch (this.mode) {
						case CONSUME_ALL:
							for (int i = 0; i < inventory.getSize(); i++) {
								ItemStack stack = inventory.getItem(i);
								if (stack != null && PICKS.contains(stack.getType())) {
									pickaxe = stack;
									break;
								}
							}
							break;
						case CONSUME_OFFHAND:
							pickaxe = inventory.getItemInOffHand();
							break;
						case ONE_DURABILITY_ALL:
							for (int i = 0; i < inventory.getSize(); i++) {
								ItemStack stack = inventory.getItem(i);
								if (stack != null && PICKS.contains(stack.getType())) {
									if (shouldDamage(stack)) {
										pickaxe = stack;
									}
								}
							}
							break;
						case ONE_DURABILITY_OFFHAND:
							ItemStack stack = player.getInventory().getItemInOffHand();
							if (shouldDamage(stack)) pickaxe = stack;
							break;
						default:
							throw new IllegalStateException("ono");
					}


					if (pickaxe == null) {
						player.sendMessage(ChatColor.RED + "no valid pickaxes!");
					} else {
						Block block = newPos.getBlock();
						Material material = block.getType();
						Collection<ItemStack> stacks;
						if (STONES.contains(material)) // break with pickaxe if needed
							stacks = block.getDrops(pickaxe);
						else if (material == Material.LAVA) stacks = Collections.singletonList(new ItemStack(Material.OBSIDIAN));
						else stacks = block.getDrops(); // otherwise break with "fist"
						stacks.forEach(i -> player.getWorld().dropItemNaturally(player.getLocation(), i)); // give player item
						damage(pickaxe);
						block.setType(Material.AIR, false);
					}
				}
			} else { // set pos
				Block clicked = event.getClickedBlock();
				if (clicked != null) {
					Location location = clicked.getRelative(event.getBlockFace()).getLocation();
					if (this.start == null) { // set first pos
						player.sendMessage(ChatColor.GREEN + "first position set!");
						this.start = location;
					} else {
						player.sendMessage(ChatColor.GREEN + "end position set!");
						this.end = location;
						this.current = new Location(this.end.getWorld(), Math.min(this.end.getX(), this.start.getX()), Math.max(this.end.getY(), this.start.getY()), Math.min(this.end.getZ(), this.start.getZ())); // starting position
					}
				}
			}
		}
	}

	private static boolean shouldDamage(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		return meta instanceof Damageable && ((Damageable) meta).getDamage() + 1 < stack.getType().getMaxDurability();
	}

	private static void damage(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		int unbreaking = meta.isUnbreakable() ? Integer.MAX_VALUE : meta.getEnchantLevel(Enchantment.DURABILITY);
		if (meta instanceof Damageable && RANDOM.nextInt(unbreaking * unbreaking + 1) == 0) {
			int damage = ((Damageable) meta).getDamage() + 1;
			if (stack.getType().getMaxDurability() == damage) {
				stack.setAmount(stack.getAmount() - 1);
			} else ((Damageable) meta).setDamage(damage);
			stack.setItemMeta(meta);
		}
	}

	enum Mode {
		CONSUME_ALL, CONSUME_OFFHAND, ONE_DURABILITY_OFFHAND, ONE_DURABILITY_ALL
	}

	@Reader (8934892891289L)
	public void read(PersistentInput input) throws IOException {
		this.start = (Location) input.readPersistent();
		this.current = (Location) input.readPersistent();
		this.end = (Location) input.readPersistent();
		this.mode = VALUES[input.readInt()];
	}

	@Writer (8934892891289L)
	public void write(PersistentOutput output) throws IOException {
		output.writePersistent(this.start);
		output.writePersistent(this.current);
		output.writePersistent(this.end);
		output.writeInt(this.mode.ordinal());
	}
}
