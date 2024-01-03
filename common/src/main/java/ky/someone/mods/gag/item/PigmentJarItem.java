package ky.someone.mods.gag.item;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

import static ky.someone.mods.gag.GAGUtil.TOOLTIP_FLAVOUR;

public class PigmentJarItem extends GAGItem {

	/**
	 * example nbt:
	 * { "pigment": { "color": 16711680, "amount": 255 } }
	 */

	public static final String PIGMENT_NBT_KEY = "pigment";
	public static final String COLOR_NBT_KEY = "color";
	public static final String AMOUNT_NBT_KEY = "amount";

	public static final int MAX_AMOUNT = 64;
	public static final int DYE_AMOUNT = 4;

	public PigmentJarItem() {
		super(new Properties().stacksTo(16));
	}

	@Nullable
	public static Pigment getPigment(ItemStack stack) {
		var pigmentTag = stack.getTagElement(PIGMENT_NBT_KEY);
		if (pigmentTag == null) return null;

		var color = pigmentTag.getInt(COLOR_NBT_KEY);
		var amount = pigmentTag.getInt(AMOUNT_NBT_KEY);

		return new Pigment(color, amount);
	}

	public static boolean isEmpty(ItemStack stack) {
		var pigment = getPigment(stack);
		return pigment == null || pigment.isEmpty();
	}

	public static int getColor(ItemStack stack) {
		var pigment = getPigment(stack);
		return pigment == null ? -1 : pigment.color;
	}

	public static int getColorAmount(ItemStack stack) {
		var pigment = getPigment(stack);
		return pigment == null ? 0 : pigment.amount;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		var pigment = getPigment(itemStack);
		if (pigment != null) {
			list.add(Component.translatable("item.gag.pigment_jar.contents",
					GAGUtil.asStyledValue(pigment.amount, MAX_AMOUNT),
					Component.literal(pigment.hex()).withStyle(s -> s.withColor(pigment.color))
			).withStyle(TOOLTIP_FLAVOUR));
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		var name = super.getName(stack);
		var pigment = getPigment(stack);

		if (pigment == null) return name;
		return name.copy().withStyle(s -> s.withColor(pigment.color));
	}

	public record Pigment(int color, int amount) {
		public boolean isEmpty() {
			return amount <= 0 || color < 0;
		}

		public String hex() {
			return String.format("#%06x", color);
		}

		@Override
		public String toString() {
			return String.format("Pigment{color=%s, amount=%d}", hex(), amount);
		}

		public float[] hsb() {
			return Color.RGBtoHSB(color >> 16 & 0xff, color >> 8 & 0xff, color >> 0 & 0xff, null);
		}

		public Pigment withAmount(int amount) {
			return new Pigment(this.color, amount);
		}

		public Pigment mix(Pigment other) {
			if (this.isEmpty()) return other;
			if (other.isEmpty()) return this;

			var newAmount = this.amount + other.amount;
			if (newAmount > MAX_AMOUNT) newAmount = MAX_AMOUNT;
			if (this.color == other.color) return new Pigment(this.color, newAmount);

			var weight = this.amount / (float) newAmount;

			var thisHsv = this.hsb();
			var otherHsv = other.hsb();

			// todo: we probably need to handle hue differently (blue + black makes a weird dark teal instead a dark blue)
			var h = (weight * thisHsv[0] + (1 - weight) * otherHsv[0]);
			var s = (weight * thisHsv[1] + (1 - weight) * otherHsv[1]);
			var b = (weight * thisHsv[2] + (1 - weight) * otherHsv[2]);

			var newColor = Color.HSBtoRGB(h, s, b) & 0xffffff;
			return new Pigment(newColor, newAmount);
		}

		public ItemStack asJar() {
			var stack = ItemRegistry.PIGMENT_JAR.get().getDefaultInstance();
			var tag = stack.getOrCreateTagElement(PIGMENT_NBT_KEY);
			tag.putInt(COLOR_NBT_KEY, this.color);
			tag.putInt(AMOUNT_NBT_KEY, this.amount);
			return stack;
		}

		public static Pigment fromDye(DyeColor dye, int amount) {
			return new Pigment(dye.getTextColor(), amount);
		}
	}

}
