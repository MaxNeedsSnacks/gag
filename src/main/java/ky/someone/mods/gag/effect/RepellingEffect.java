package ky.someone.mods.gag.effect;

import ky.someone.mods.gag.GAGRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class RepellingEffect extends MobEffect {
	public RepellingEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xefc648);
	}

	public static boolean applyRepel(LivingEntity entity, LevelAccessor level, double x, double y, double z) {
		if (entity instanceof Enemy) {
			var pos = new Vec3(x, y, z);
			for (var player : level.players()) {
				var repel = player.getEffect(GAGRegistry.REPELLING);
				if (repel != null) {
					var distance = pos.distanceToSqr(player.position());
					var repelRange = 16 * (repel.getAmplifier() + 1); // TODO: make configurable
					if (distance < repelRange * repelRange) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
