package ky.someone.mods.gag.entity;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface EntityTypeRegistry {
	DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, GAGUtil.MOD_ID);

	Supplier<EntityType<TimeAcceleratorEntity>> TIME_ACCELERATOR =
			ENTITIES.register("time_accelerator", () -> EntityType.Builder.of(TimeAcceleratorEntity::new, MobCategory.MISC)
					.sized(0.1f, 0.1f)
					.noSummon()
					.build(GAGUtil.id("time_accelerator").toString()));      // no serialisation means no data fixers

	Supplier<EntityType<MiningDynamiteEntity>> MINING_DYNAMITE =
			ENTITIES.register("mining_dynamite", () -> EntityType.Builder.<MiningDynamiteEntity>of(MiningDynamiteEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
					.build(GAGUtil.id("mining_dynamite").toString()));

	Supplier<EntityType<FishingDynamiteEntity>> FISHING_DYNAMITE =
			ENTITIES.register("fishing_dynamite", () -> EntityType.Builder.<FishingDynamiteEntity>of(FishingDynamiteEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
					.build(GAGUtil.id("fishing_dynamite").toString()));
}
