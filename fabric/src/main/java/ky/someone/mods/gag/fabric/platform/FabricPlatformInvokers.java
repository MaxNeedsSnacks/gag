package ky.someone.mods.gag.fabric.platform;

import ky.someone.mods.gag.platform.PlatformInvokers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

public class FabricPlatformInvokers implements PlatformInvokers {
	public boolean invokeExplosionPre(Level level, Explosion explosion) {
		// NYI on fabric, so NOP
		return false;
	}

	@Override
	public void invokeExplosionPost(Level level, Explosion explosion, List<Entity> entities, double diameter) {

	}
}
