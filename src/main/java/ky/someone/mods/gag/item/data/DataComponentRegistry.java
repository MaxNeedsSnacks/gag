package ky.someone.mods.gag.item.data;

import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.misc.Pigment;
import ky.someone.mods.gag.misc.TeleportPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface DataComponentRegistry {
	DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(GAGUtil.MOD_ID);

	Supplier<DataComponentType<Pigment>> PIGMENT = COMPONENTS
			.registerComponentType("pigment", (builder) -> builder
					.persistent(Pigment.CODEC)
					.networkSynchronized(Pigment.STREAM_CODEC)
			);

	Supplier<DataComponentType<Integer>> GRAINS_OF_TIME = COMPONENTS
			.registerComponentType("grains_of_time", builder -> builder
					.persistent(ExtraCodecs.POSITIVE_INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)
			);

	Supplier<DataComponentType<TeleportPos>> TELEPORT_TARGET = COMPONENTS
			.registerComponentType("teleport_target", builder -> builder
					.persistent(TeleportPos.CODEC)
					.networkSynchronized(TeleportPos.STREAM_CODEC)
			);
}
