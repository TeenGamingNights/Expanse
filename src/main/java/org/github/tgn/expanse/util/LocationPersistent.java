package org.github.tgn.expanse.util;

import net.devtech.yajslib.annotations.DependsOn;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import net.devtech.yajslib.persistent.Persistent;
import org.bukkit.Location;
import org.bukkit.World;
import java.io.IOException;

@DependsOn(World.class)
public class LocationPersistent implements Persistent<Location> {
	private final long versionHash;

	public LocationPersistent(long hash) {this.versionHash = hash;}

	@Override
	public long versionHash() {
		return this.versionHash;
	}

	@Override
	public void write(Location location, PersistentOutput output) throws IOException {
		output.writePersistent(location.getWorld(), true);
		output.writeDouble(location.getX());
		output.writeDouble(location.getY());
		output.writeDouble(location.getZ());
	}

	@Override
	public Location read(PersistentInput input) throws IOException {
		return new Location((World) input.readPersistent(), input.readDouble(), input.readDouble(), input.readDouble());
	}

	@Override
	public Location blank() {
		return new Location(null, 0, 0, 0);
	}
}
