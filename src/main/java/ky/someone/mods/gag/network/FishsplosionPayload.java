package ky.someone.mods.gag.network;

import io.netty.buffer.ByteBuf;
import ky.someone.mods.gag.entity.FishingDynamiteEntity;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FishsplosionPayload(Vec3 pos, float radius) implements PayloadWithHandler {
	public FishsplosionPayload(Explosion explosion) {
		this(explosion.center(), explosion.radius());
	}

	public static final StreamCodec<ByteBuf, FishsplosionPayload> CODEC = StreamCodec.composite(
			StreamCodec.composite(
					ByteBufCodecs.DOUBLE, Vec3::x,
					ByteBufCodecs.DOUBLE, Vec3::y,
					ByteBufCodecs.DOUBLE, Vec3::z,
					Vec3::new
			),
			FishsplosionPayload::pos,
			ByteBufCodecs.FLOAT,
			FishsplosionPayload::radius,
			FishsplosionPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return GAGNetwork.FISHSPLOSION;
	}

	@Override
	public void handle(IPayloadContext ctx) {
		var explosion = new FishingDynamiteEntity.Fishsplosion(ctx.player().level(), null, this.pos.x, this.pos.y, this.pos.z, this.radius);
		explosion.finalizeExplosion(true);
	}
}
