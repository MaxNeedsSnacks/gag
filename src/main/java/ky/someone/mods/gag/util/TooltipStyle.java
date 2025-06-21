package ky.someone.mods.gag.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.function.UnaryOperator;

public record TooltipStyle(UnaryOperator<Style> wrapped) implements UnaryOperator<Style> {
	@Override
	public Style apply(Style style) {
		return wrapped.apply(style);
	}

	static TooltipStyle color(int color) {
		return new TooltipStyle(style -> style.withColor(color));
	}

	public MutableComponent apply(String text) {
		return literal(text);
	}

	public MutableComponent apply(MutableComponent component) {
		return component.withStyle(this);
	}

	public MutableComponent literal(String text) {
		return Component.literal(text).withStyle(this);
	}

	public MutableComponent lang(String key, Object... args) {
		return Component.translatable(key, args).withStyle(this);
	}
}
