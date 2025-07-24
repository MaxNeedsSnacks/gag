package ky.someone.mods.gag.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ky.someone.mods.gag.util.Tooltips;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record TeleportPos(ResourceKey<Level> level, Vec3 pos, float yaw) {
	public static final Codec<TeleportPos> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Level.RESOURCE_KEY_CODEC.fieldOf("level").forGetter(TeleportPos::level),
			Vec3.CODEC.fieldOf("pos").forGetter(TeleportPos::pos),
			Codec.FLOAT.fieldOf("yaw").forGetter(TeleportPos::yaw)
	).apply(builder, TeleportPos::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPos> STREAM_CODEC = StreamCodec.composite(
			ResourceKey.streamCodec(Registries.DIMENSION),
			TeleportPos::level,
			ByteBufCodecs.fromCodec(Vec3.CODEC),
			TeleportPos::pos,
			ByteBufCodecs.FLOAT,
			TeleportPos::yaw,
			TeleportPos::new
	);

	public MutableComponent formatPos() {
		return Tooltips.SUCCESS.apply(String.format("(%.1f %.1f %.1f)", pos.x, pos.y, pos.z));
	}

	public MutableComponent formatLevel() {
		var dim = level.location();
		return Component.translatableWithFallback(Util.makeDescriptionId("dimension", dim), dim.toString());
	}

	public MutableComponent formatFull() {
		return formatPos().append(Tooltips.FLAVOUR.apply(" @ ").append(formatLevel()));
	}
}
