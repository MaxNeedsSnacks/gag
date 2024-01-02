package ky.someone.mods.gag.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public interface GAGSounds {
	DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.SOUND_EVENT);

	RegistrySupplier<SoundEvent> DYNAMITE_THROW = register("entity.dynamite.throw");

	RegistrySupplier<SoundEvent> HEARTHSTONE_THUNDER = register("item.hearthstone.thunder");
	RegistrySupplier<SoundEvent> REPELLING_APPLY = register("item.repelling.apply");

	RegistrySupplier<SoundEvent> TELEPORT = register("generic.teleport");
	RegistrySupplier<SoundEvent> TELEPORT_FAIL = register("generic.teleport.fail");

	private static RegistrySupplier<SoundEvent> register(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(GAGUtil.id(name)));
	}
}
