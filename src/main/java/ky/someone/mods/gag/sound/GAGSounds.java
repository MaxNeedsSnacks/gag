package ky.someone.mods.gag.sound;


import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface GAGSounds {
	DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, GAGUtil.MOD_ID);

	Supplier<SoundEvent> DYNAMITE_THROW = register("entity.dynamite.throw");

	Supplier<SoundEvent> HEARTHSTONE_THUNDER = register("item.hearthstone.thunder");
	Supplier<SoundEvent> REPELLING_APPLY = register("item.repelling.apply");

	Supplier<SoundEvent> TELEPORT = register("generic.teleport");
	Supplier<SoundEvent> TELEPORT_FAIL = register("generic.teleport.fail");

	private static Supplier<SoundEvent> register(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(GAGUtil.id(name)));
	}
}
