package ky.someone.mods.gag.forge.platform;

import ky.someone.mods.gag.platform.PlatformInvokers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class ForgePlatformInvokers implements PlatformInvokers {
	public boolean invokeExplosionPre(Level level, Explosion explosion) {
		return ForgeEventFactory.onExplosionStart(level, explosion);
	}

	@Override
	public void invokeExplosionPost(Level level, Explosion explosion, List<Entity> entities, double diameter) {
		ForgeEventFactory.onExplosionDetonate(level, explosion, entities, diameter);
	}
}
