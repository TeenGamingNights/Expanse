package org.github.tgn.expanse.entities;

import net.devtech.yajslib.annotations.Reader;
import net.devtech.yajslib.annotations.Writer;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import org.bukkit.entity.Skeleton;
import org.github.tgn.expanse.attacks.*;
import java.io.IOException;

public class BasicBoss extends ComplexEntity<Skeleton> {
	public BasicBoss() {
		this.attacks.add(new ArrowStormAttack(10), 1);
		this.attacks.add(new FreezeAttack(), 1);
		this.attacks.add(new LaunchAttack(), 1);
		this.attacks.add(new ParticleAttack.Ignite(), 1);
		this.attacks.add(new ParticleAttack.Poison(), 1);
		this.attacks.add(new ParticleAttack.Wither(), 1);
		this.attacks.add(new SelfDestructAttack(), 1);
		this.attacks.add(new SmiteAttack(10), 1);
		this.attacks.add(new Teleport(10), 1);
	}

	@Override
	public void attach(Skeleton entity) {
		entity.setMaxHealth(100);
		entity.setHealth(100);
	}

	@Reader(182919109209L)
	public void read(PersistentInput in) throws IOException {
		System.out.println("read: " + in.readInt());
	}

	@Writer(182919109209L)
	public void write(PersistentOutput out) throws IOException {
		out.writeInt(4);
	}
}
