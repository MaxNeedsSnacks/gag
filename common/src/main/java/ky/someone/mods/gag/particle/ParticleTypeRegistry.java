package ky.someone.mods.gag.particle;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public interface ParticleTypeRegistry {
	DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(GAGUtil.MOD_ID, Registries.PARTICLE_TYPE);

	RegistrySupplier<SimpleParticleType> MAGIC = PARTICLE_TYPES.register("magic", () -> new SimpleParticleType(true));
}
