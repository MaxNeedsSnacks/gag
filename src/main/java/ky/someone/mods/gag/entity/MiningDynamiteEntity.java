package ky.someone.mods.gag.entity;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MiningDynamiteEntity extends AbstractDynamiteEntity {

	public static final TagKey<Block> MINING_DYNAMITE_EFFECTIVE = TagKey.create(Registries.BLOCK, GAGUtil.id("mining_dynamite_effective"));

	public MiningDynamiteEntity(EntityType<? extends MiningDynamiteEntity> type, Level level) {
		super(type, level);
	}

	public MiningDynamiteEntity(double x, double y, double z, Level level) {
		super(GAGRegistry.MINING_DYNAMITE.get(), x, y, z, level);
	}

	public MiningDynamiteEntity(LivingEntity owner, Level level) {
		super(GAGRegistry.MINING_DYNAMITE.get(), owner, level);
	}

	@Override
	public void tick() {
		super.tick();
		Vec3 vec3 = this.getDeltaMovement();
		// add some smoke particles above the entity to make it look nicer
		level().addParticle(ParticleTypes.SMOKE,
				getX(-vec3.x) + random.nextDouble() * 0.6 - 0.3,
				getY(-vec3.y) + random.nextDouble() * getBbHeight(),
				getZ(-vec3.z) + random.nextDouble() * 0.6 - 0.3,
				vec3.x, vec3.y, vec3.z
		);
	}

	@Override
	protected void onHitEntity(EntityHitResult hitEntity) {
		super.onHitEntity(hitEntity);
		if (GAGConfig.dynamite.miningGivesHaste() && hitEntity.getEntity() instanceof LivingEntity entity) {
			entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 160, 1, false, false));
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 1, false, false));
		}
	}

	@Override
	public void detonate(Vec3 pos) {
		var r = GAGConfig.dynamite.miningRadius();
		var level = level();
		var explosion = new BlockMiningExplosion(level, this, pos.x, pos.y, pos.z, r);
		if (!EventHooks.onExplosionStart(level, explosion)) {
			explosion.explode();

			explosion.finalizeExplosion(false);
			for (Player player : level.players()) {
				if (player.distanceToSqr(this) < 4096.0D) {
					((ServerPlayer) player).connection.send(new ClientboundExplodePacket(
							pos.x, pos.y, pos.z, r,
							explosion.getToBlow(),
							null,
							explosion.getBlockInteraction(),
							explosion.getSmallExplosionParticles(),
							explosion.getLargeExplosionParticles(),
							explosion.getExplosionSound()
					));
				}
			}
		}
	}

	@Override
	protected Item getDefaultItem() {
		return GAGRegistry.MINING_DYNAMITE_ITEM.get();
	}

	private static class BlockMiningExplosion extends Explosion {
		public BlockMiningExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float radius) {
			super(level, entity, null, new ExplosionDamageCalculator() {
				@Override
				public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
					if (!fluidState.isEmpty()) {
						return Optional.empty();
					}

					var orig = super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState);

					return orig.map(f -> {
						if (blockState.is(MINING_DYNAMITE_EFFECTIVE)) {
							return f * 0.75f;
						}
						return f;
					});
				}

				@Override
				public boolean shouldBlockExplode(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, float f) {
					return blockState.getFluidState().isEmpty() && super.shouldBlockExplode(explosion, blockGetter, blockPos, blockState, f);
				}

				@Override
				public boolean shouldDamageEntity(Explosion arg, Entity arg2) {
					return false;
				}
			}, x, y, z, radius, false, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
		}
	}
}

