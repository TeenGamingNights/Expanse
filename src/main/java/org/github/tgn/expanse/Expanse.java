package org.github.tgn.expanse;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.github.tgn.expanse.world.VoidGenerator;
import java.util.Random;

public final class Expanse extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		// Plugin startup logic
		System.out.println("Expanse init!");
		WorldCreator creator = new WorldCreator("expanse");
		creator.generator(new VoidGenerator(Biome.values()));
		World world = Bukkit.createWorld(creator);
		this.getCommand("warp").setExecutor((sender, command, label, args) -> {
			if(sender instanceof Player) {
				Location location = ((Player) sender).getLocation();
				location.setWorld(world);
				((Player) sender).teleport(location);
				location.add(0, -1, 0);
				world.getBlockAt(location).setType(Material.STONE);
				return true;
			}
			return false;
		});
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
