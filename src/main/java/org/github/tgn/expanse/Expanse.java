package org.github.tgn.expanse;

import net.devtech.yajslib.persistent.AnnotatedPersistent;
import net.devtech.yajslib.persistent.PersistentRegistry;
import net.devtech.yajslib.persistent.SimplePersistentRegistry;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.github.tgn.expanse.entities.BasicBoss;
import org.github.tgn.expanse.entities.CustomEntity;
import org.github.tgn.expanse.world.VoidGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Expanse extends JavaPlugin implements Listener {
	public static Expanse instance;
	public static final PersistentRegistry PERSISTENT_REGISTRY = new SimplePersistentRegistry();
	public static final EntityManager ENTITY_MANAGER = new EntityManager(PERSISTENT_REGISTRY);
	public static final Map<String, Supplier<CustomEntity>> SUPPLIER_MAP = new HashMap<>();
	static {
		PERSISTENT_REGISTRY.register(BasicBoss.class, new AnnotatedPersistent<>(BasicBoss::new, BasicBoss.class, 182919109209L));
		SUPPLIER_MAP.put("basic", BasicBoss::new);
	}

	@SuppressWarnings ("ConstantConditions")
	@Override
	public void onEnable() {
		instance = this;
		// Plugin startup logic
		System.out.println("Expanse init!");
		WorldCreator creator = new WorldCreator("expanse");
		creator.generator(new VoidGenerator(Biome.values()));
		World theExpanse = Bukkit.createWorld(creator);
		this.getCommand("warp").setExecutor((sender, command, label, args) -> {
			if (sender instanceof Player) {
				Location location = ((Player) sender).getLocation();
				location.setWorld(theExpanse);
				((Player) sender).teleport(location);
				location.add(0, -1, 0);
				theExpanse.getBlockAt(location).setType(Material.STONE);
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
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			ENTITY_MANAGER.tick(theExpanse);
		}, 1, 8);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
