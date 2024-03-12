package ky.someone.mods.gag.particle;


import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ParticleTypeRegistry {
	DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, GAGUtil.MOD_ID);

	Supplier<SimpleParticleType> MAGIC = PARTICLE_TYPES.register("magic", () -> new SimpleParticleType(true));
}
