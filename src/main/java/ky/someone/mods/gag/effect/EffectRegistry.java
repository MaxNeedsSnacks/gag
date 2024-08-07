package ky.someone.mods.gag.effect;


import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface EffectRegistry {
	DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, GAGUtil.MOD_ID);

	Holder<MobEffect> REPELLING = EFFECTS.register("repelling", RepellingEffect::new);
}
