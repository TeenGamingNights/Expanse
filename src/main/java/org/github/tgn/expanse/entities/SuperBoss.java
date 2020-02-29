package org.github.tgn.expanse.entities;

import net.devtech.yajslib.annotations.Reader;
import net.devtech.yajslib.annotations.Writer;
import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.github.tgn.expanse.Expanse;
import org.github.tgn.expanse.attacks.*;
import java.io.IOException;

public class SuperBoss extends ComplexEntity<LivingEntity> {
	public SuperBoss() {
		if (RANDOM.nextBoolean()) this.attacks.add(new ArrowStormAttack(15), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new FreezeAttack(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new LaunchAttack(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new ParticleAttack.Ignite(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new ParticleAttack.Poison(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new ParticleAttack.Wither(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new SelfDestructAttack(), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new SmiteAttack(10), 1);
		if (RANDOM.nextBoolean()) this.attacks.add(new Teleport(10), 1);
	}

	@Override
	public void attach(LivingEntity entity) {
		entity.setMaxHealth(100);
		entity.setHealth(100);
	}

	@Reader (182919109209L)
	public void read(PersistentInput in) throws IOException {
		System.out.println("read: " + in.readInt());
	}

	@Writer (182919109209L)
	public void write(PersistentOutput out) throws IOException {
		out.writeInt(4);
	}

	@Override
	public void die(EntityDeathEvent event) {
		if(RANDOM.nextInt(25) == 0)
			event.getDrops().add(Expanse.CUSTOM_ITEMS.get(Expanse.ITEM_IDS.get(RANDOM.nextInt(Expanse.ITEM_IDS.size()))).get());
	}
}
