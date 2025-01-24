package ky.someone.mods.gag.platform;

import dev.architectury.event.events.common.ExplosionEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import oshi.util.Memoizer;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface PlatformInvokers {

	Supplier<PlatformInvokers> INSTANCE = Memoizer.memoize(() -> ServiceLoader.load(PlatformInvokers.class).findFirst()
			.orElseThrow(() -> new IllegalStateException("No PlatformInvokers implementation found!")));

	boolean invokeExplosionPre(Level level, Explosion explosion);

	void invokeExplosionPost(Level level, Explosion explosion, List<Entity> entities, double diameter);

	static PlatformInvokers get() {
		return INSTANCE.get();
	}

	static boolean explosionPre(Level level, Explosion explosion) {
		return get().invokeExplosionPre(level, explosion) || ExplosionEvent.PRE.invoker().explode(level, explosion).isTrue();
	}

	static void explosionPost(Level level, Explosion explosion, List<Entity> entities, double diameter) {
		get().invokeExplosionPost(level, explosion, entities, diameter);
		ExplosionEvent.DETONATE.invoker().explode(level, explosion, entities);
	}
}
