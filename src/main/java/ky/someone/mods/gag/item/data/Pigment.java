package ky.someone.mods.gag.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Objects;

// not a record so i can change the underlying colour representation later
public final class Pigment {
	private final int color;
	private final int amount;

	public static final Pigment EMPTY = new Pigment(-1, 0);

	public static final Codec<Pigment> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(
					Codec.INT.fieldOf("color").forGetter(Pigment::color),
					Codec.INT.fieldOf("amount").forGetter(Pigment::amount)
			).apply(builder, Pigment::new)
	);

	public static final StreamCodec<FriendlyByteBuf, Pigment> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			Pigment::color,
			ByteBufCodecs.INT,
			Pigment::amount,
			Pigment::new
	);

	private Pigment(int color, int amount) {
		this.color = color;
		this.amount = amount;
	}

	public static Pigment ofRgb(int color, int amount) {
		return new Pigment(color, amount);
	}

	public boolean isEmpty() {
		return amount <= 0 || color < 0;
	}

	public int rgb() {
		return color(); // remember to update this as well!
	}

	public String hex() {
		return String.format("#%06x", rgb());
	}

	@Override
	public String toString() {
		return String.format("Pigment{rgb=%s, amount=%d}", hex(), amount);
	}

	public float[] hsb() {
		var rgb = this.rgb();
		return Color.RGBtoHSB(rgb >> 16 & 0xff, rgb >> 8 & 0xff, rgb >> 0 & 0xff, null);
	}

	public Pigment withAmount(int amount) {
		return new Pigment(this.color(), amount);
	}

	public Pigment mix(@Nullable Pigment other) {
		if (this.isEmpty()) return other;
		if (other == null || other.isEmpty()) return this;

		var newAmount = this.amount + other.amount;
		if (newAmount > PigmentJarItem.MAX_AMOUNT) newAmount = PigmentJarItem.MAX_AMOUNT;
		if (this.color() == other.color()) return new Pigment(this.color(), newAmount);

		var weight = this.amount / (float) newAmount;

		var thisHsv = this.hsb();
		var otherHsv = other.hsb();

		// todo: this is better, but still not perfect
		var hDelta = Math.abs(thisHsv[0] - otherHsv[0]);
		if (hDelta > 0.5) {
			// hue is on a circle, so we need to wrap around
			if (thisHsv[0] > otherHsv[0]) {
				otherHsv[0] += 1;
			} else {
				thisHsv[0] += 1;
			}
		}
		var h = (weight * thisHsv[0] + (1 - weight) * otherHsv[0]);
		var s = (weight * thisHsv[1] + (1 - weight) * otherHsv[1]);
		var b = (weight * thisHsv[2] + (1 - weight) * otherHsv[2]);

		var newColor = Color.HSBtoRGB(h, s, b) & 0xffffff;
		return new Pigment(newColor, newAmount);
	}

	public ItemStack asJar() {
		var stack = GAGRegistry.PIGMENT_JAR.get().getDefaultInstance();
		if (this.isEmpty()) return stack;
		stack.set(GAGRegistry.PIGMENT_DATA, this);
		return stack;
	}

	public static Pigment forText(DyeColor dye) {
		return new Pigment(dye.getTextColor(), PigmentJarItem.DYE_AMOUNT);
	}

	public static Pigment forLeather(DyeColor dye) {
		return new Pigment(dye.getTextureDiffuseColor(), PigmentJarItem.DYE_AMOUNT);
	}

	public int color() {
		return color;
	}

	public int amount() {
		return amount;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other instanceof Pigment pigment) {
			return pigment.color == this.color
			       && pigment.amount == this.amount;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, amount);
	}
}
