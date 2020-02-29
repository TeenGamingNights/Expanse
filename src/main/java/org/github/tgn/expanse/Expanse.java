package org.github.tgn.expanse;

import net.devtech.yajslib.persistent.AnnotatedPersistent;
import net.devtech.yajslib.persistent.PersistentRegistry;
import net.devtech.yajslib.persistent.SimplePersistentRegistry;
import net.devtech.yajslib.persistent.util.StringPersistent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.github.tgn.expanse.entities.CustomEntity;
import org.github.tgn.expanse.entities.SuperBoss;
import org.github.tgn.expanse.items.*;
import org.github.tgn.expanse.items.custom.*;
import org.github.tgn.expanse.util.EmptyPersistent;
import org.github.tgn.expanse.util.LocationPersistent;
import org.github.tgn.expanse.util.WorldPersistent;
import org.github.tgn.expanse.world.VoidGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Expanse extends JavaPlugin implements Listener {
	public static Expanse instance;
	public static final PersistentRegistry PERSISTENT_REGISTRY = new SimplePersistentRegistry();
	public static final EntityManager ENTITY_MANAGER = new EntityManager(PERSISTENT_REGISTRY);
	public static final Map<String, Supplier<CustomEntity>> SUPPLIER_MAP = new HashMap<>();
	public static final List<Function<Location, Pair<CustomEntity, Entity>>> SPAWN = new ArrayList<>();
	public static final Map<String, Supplier<ItemStack>> CUSTOM_ITEMS = new HashMap<>();
	public static final ArrayList<String> ITEM_IDS = new ArrayList<>();

	private World theExpanse;

	static {
		PERSISTENT_REGISTRY.register(SuperBoss.class, new AnnotatedPersistent<>(SuperBoss::new, SuperBoss.class, 182919109209L));
		PERSISTENT_REGISTRY.register(String.class, new StringPersistent(328991291L));
		PERSISTENT_REGISTRY.register(World.class, new WorldPersistent(1811990L));
		PERSISTENT_REGISTRY.register(Location.class, new LocationPersistent(320920909143723L));
		PERSISTENT_REGISTRY.register(EaterOfWorlds.class, new AnnotatedPersistent<>(EaterOfWorlds::new, EaterOfWorlds.class, 8934892891289L));
		PERSISTENT_REGISTRY.register(Arsenic.class, new EmptyPersistent<>(38929209L, Arsenic::new));
		PERSISTENT_REGISTRY.register(Dynamite.class, new EmptyPersistent<>(342032L, Dynamite::new));
		PERSISTENT_REGISTRY.register(Glowstick.class, new EmptyPersistent<>(2901040L, Glowstick::new));
		PERSISTENT_REGISTRY.register(Ketamine.class, new EmptyPersistent<>(43092091L, Ketamine::new));
		PERSISTENT_REGISTRY.register(SmeltingTool.class, new AnnotatedPersistent<>(() -> new SmeltingTool(null), SmeltingTool.class, 439209120920L));
		SUPPLIER_MAP.put("basic", SuperBoss::new);
		EntityType[] lists = {EntityType.SKELETON, EntityType.ZOMBIE, EntityType.CREEPER, EntityType.WITHER_SKELETON, EntityType.WITCH, EntityType.SPIDER, EntityType.BLAZE, EntityType.GHAST};
		SPAWN.add(l -> {
			Skeleton entity = (Skeleton) l.getWorld().spawnEntity(l, lists[CustomItem.RANDOM.nextInt(lists.length)]);
			EntityEquipment equipment = entity.getEquipment();
			equipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			return new Pair<>(new SuperBoss(), entity);
		});

		register("worldeater", EaterOfWorlds::new);
		register("arsenic", Arsenic::new);
		register("dynamite", Dynamite::new);
		register("glowstick", Glowstick::new);
		register("ketamine", Ketamine::new);

		register("magmaxe", () -> new SmeltingTool(Material.GOLDEN_AXE));
		register("lavapick", () -> new SmeltingTool(Material.IRON_PICKAXE));
		register("magmashovel", () -> new SmeltingTool(Material.GOLDEN_SHOVEL));
	}

	private static void register(String key, Supplier<CustomItem> itemSupplier) {
		ITEM_IDS.add(key);
		CUSTOM_ITEMS.put(key, () -> CustomItemFactory.wrap(PERSISTENT_REGISTRY, itemSupplier.get()));
	}

	@SuppressWarnings ("ConstantConditions")
	@Override
	public void onEnable() {
		ENTITY_MANAGER.registerEntityEvent(this, EntityDamageEvent.class, EntityEvent::getEntity, CustomEntity::damage);
		ENTITY_MANAGER.registerEntityEvent(this, EntityDeathEvent.class, EntityDeathEvent::getEntity, CustomEntity::die);
		ItemEventManager manager = new ItemEventManager(this, PERSISTENT_REGISTRY);
		manager.register(PlayerInteractEvent.class, PlayerInteractEvent::getItem, c -> c instanceof CanInteractWith, CanInteractWith::interact, EventPriority.NORMAL, false);
		manager.register(BlockBreakEvent.class, e -> e.getPlayer().getInventory().getItemInMainHand(), c -> c instanceof CanBreakWith, CanBreakWith::destroy, EventPriority.NORMAL, false);
		instance = this;
		// Plugin startup logic
		System.out.println("Expanse init!");
		WorldCreator creator = new WorldCreator("world_expanse");
		creator.generator(new VoidGenerator(Biome.values()));
		this.theExpanse = Bukkit.createWorld(creator);
		this.getCommand("warp").setExecutor((sender, command, label, args) -> {
			if (sender instanceof Player) {
				Location location = ((Player) sender).getLocation();
				location.setWorld(this.theExpanse);
				((Player) sender).teleport(location);
				location.add(0, -1, 0);
				this.theExpanse.getBlockAt(location).setType(Material.STONE);
				return true;
			}
			return false;
		});
		this.getCommand("create").setExecutor(((sender, command, label, args) -> {
			if (args.length != 2) return false;
			EntityType type = EntityType.fromName(args[1]);
			Supplier<CustomEntity> entitySupplier = SUPPLIER_MAP.get(args[0]);
			if (sender instanceof Entity && type != null && entitySupplier != null) {
				Entity entity = (Entity) sender;
				Entity newEntity = entity.getWorld().spawnEntity(entity.getLocation(), type);
				ENTITY_MANAGER.attach(newEntity, entitySupplier.get());
				return true;
			} else return false;
		}));
		this.getCommand("cheat").setExecutor((sender, command, label, args) -> {
			if (sender instanceof InventoryHolder && args.length == 1) {
				Inventory inventory = ((InventoryHolder) sender).getInventory();
				inventory.addItem(CUSTOM_ITEMS.get(args[0]).get());
				return true;
			}
			return false;
		});
		Bukkit.getScheduler().runTaskTimer(this, () -> ENTITY_MANAGER.tick(this.theExpanse), 1, 40);
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static class Pair<A, B> {
		public Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}

		public final A a;
		public final B b;

	}
}
