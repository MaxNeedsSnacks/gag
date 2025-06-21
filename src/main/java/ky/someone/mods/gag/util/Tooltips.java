package ky.someone.mods.gag.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public interface Tooltips {
	TooltipStyle MAIN = TooltipStyle.color(0xfcb95b);
	TooltipStyle EXTRA = new TooltipStyle(style -> style.withColor(0x0fd1ec).withItalic(true));
	TooltipStyle FLAVOUR = new TooltipStyle(style -> style.withColor(ChatFormatting.GRAY));
	TooltipStyle SUCCESS = TooltipStyle.color(0x4ecc8d);
	TooltipStyle FAIL = TooltipStyle.color(0xfd6d5d);
	TooltipStyle INFO = TooltipStyle.color(0x5555ff);

	Component TRUE = SUCCESS.literal("✔");
	Component FALSE = SUCCESS.literal("✘");

	static MutableComponent bool(boolean value) {
		return (value ? TRUE : FALSE).copy();
	}

	// returns a new style using "parent" with a colour representing
	// the given ratio of the range [0, 1] (0 = red, 1 = green (clamped at 1.5 = cyan))
	static Style styledRatio(Style parent, double ratio) {
		var clampedRatio = Mth.clamp(ratio, 0, 1.5);
		return parent.withColor(Mth.hsvToRgb((float) clampedRatio / 3, 1, 1));
	}

	static Component asStyledValue(double value, double max) {
		return asStyledValue(value, max, Double.toString(value));
	}

	static Component asStyledValue(double value, double max, String formattedValue) {
		return literal(formattedValue).withStyle(styledRatio(Style.EMPTY, value / max));
	}

	static Component asStyledLang(double value, double max, String formattedValue) {
		return translatable(formattedValue).withStyle(styledRatio(Style.EMPTY, value / max));
	}
}
