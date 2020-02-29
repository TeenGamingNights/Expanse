package org.github.tgn.expanse.util;

import net.devtech.yajslib.annotations.DependsOn;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import net.devtech.yajslib.persistent.Persistent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.io.IOException;

@DependsOn(String.class)
public class WorldPersistent implements Persistent<World> {
	private final long versionHash;

	public WorldPersistent(long hash) {this.versionHash = hash;}

	@Override
	public long versionHash() {
		return this.versionHash;
	}

	@Override
	public void write(World world, PersistentOutput output) throws IOException {
		output.writePersistent(world.getName());
	}

	@Override
	public World read(PersistentInput input) throws IOException {
		return Bukkit.getWorld((String) input.readPersistent());
	}

	@Override
	public World blank() {
		return Bukkit.getWorlds().get(0);
	}
}
