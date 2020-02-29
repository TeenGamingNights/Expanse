package org.github.tgn.expanse;

import net.devtech.utilib.functions.TriConsumer;
import net.devtech.yajslib.persistent.Persistent;
import net.devtech.yajslib.persistent.PersistentRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.github.tgn.expanse.entities.CustomEntity;
import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Logger;

public class EntityManager implements Listener {
	private static final Logger LOGGER = Logger.getLogger(EntityManager.class.getSimpleName());
	private final NamespacedKey pid;
	private final NamespacedKey psd;
	private final PersistentRegistry registry;

	public EntityManager(PersistentRegistry registry) {
		this.registry = registry;
		this.pid = new NamespacedKey("expanse","persistent.id");
		this.psd = new NamespacedKey("expanse", "persistent.serialized.data");
	}

	public void tick(World world) {
		for (Player player : world.getPlayers()) {
			if(player.getGameMode() == GameMode.CREATIVE)
				continue;
			BoundingBox box = player.getBoundingBox();
			box.expand(32);
			for (Entity entity : world.getNearbyEntities(box)) {
				CustomEntity customEntity = this.parse(entity);
				if (customEntity != null) customEntity.attack(player, entity);
			}
		}
	}

	@SuppressWarnings ("unchecked")
	public <T extends CustomEntity> void attach(Entity entity, T customEntity) {
		try {
			customEntity.attach(entity);
			PersistentDataContainer container = entity.getPersistentDataContainer();
			Persistent<T> persistent = (Persistent<T>) this.registry.forClass(customEntity.getClass(), false);
			byte[] array = persistent.toByteArray(this.registry, customEntity);
			container.set(this.pid, PersistentDataType.LONG, persistent.versionHash());
			container.set(this.psd, PersistentDataType.BYTE_ARRAY, array);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <E extends Event> void registerEntityEvent(Plugin plugin, Class<E> eventClass, Function<E, Entity> eventFunction, TriConsumer<CustomEntity, E, Entity> executor) {
		Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL, (listener, event) -> {
			Entity entity = eventFunction.apply((E) event);
			CustomEntity custom = this.parse(entity);
			if (custom != null) executor.accept(custom, (E) event, entity);
		}, plugin);
	}

	public CustomEntity parse(Entity entity) {
		try {
			PersistentDataContainer container = entity.getPersistentDataContainer();
			Long id = container.get(this.pid, PersistentDataType.LONG);
			if (id != null) {
				Persistent<?> persistent = this.registry.fromId(id);
				byte[] data = container.get(this.psd, PersistentDataType.BYTE_ARRAY);
				assert data != null;
				return (CustomEntity) persistent.fromByteArray(this.registry, data);
			}
		} catch (IOException e) {
			LOGGER.warning(entity + " threw an exception when deserializing, deleting now");
			entity.remove();
			e.printStackTrace();
		}
		return null;
	}

}
