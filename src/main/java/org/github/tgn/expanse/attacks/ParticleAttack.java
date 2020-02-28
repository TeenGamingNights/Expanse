package org.github.tgn.expanse.attacks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.github.tgn.expanse.entities.CustomEntity;

public interface ParticleAttack extends Attack<CustomEntity> {
	@Override
	default void attack(CustomEntity instance, Player player, Entity entity) {
		World world = entity.getWorld();
		Location relative = entity.getLocation(); // current location
		relative.subtract(player.getLocation()); // vectorify
		Vector normalizedVector = relative.toVector();
		double length = normalizedVector.length();
		normalizedVector.normalize(); // unit for iteration
		for (double i = 0; i < length; i+=.1) {
			Vector current = new Vector(0, 0, 0).copy(normalizedVector);
			current.multiply(i);
			Location particle = entity.getLocation().add(current);
			particle.add(player.getLocation());
			world.spawnParticle(this.getParticleType(), particle, 1, this.dataType());
		}
		this.inflict(player);
	}

	Particle getParticleType();

	void inflict(Player player);

	default Object dataType() {return null;}

	class Ignite implements ParticleAttack {

		@Override
		public Particle getParticleType() {
			return Particle.FLAME;
		}

		@Override
		public void inflict(Player player) {
			player.setFireTicks(60);
		}
	}

	class Wither implements ParticleAttack {

		@Override
		public Particle getParticleType() {
			return Particle.BLOCK_DUST;
		}

		@Override
		public void inflict(Player player) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 1));
		}
	}

	class Poison implements ParticleAttack {
		@Override
		public Particle getParticleType() {
			return Particle.PORTAL;
		}

		@Override
		public void inflict(Player player) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
		}
	}
}