package ky.someone.mods.gag.util.tooltip;

import com.mojang.datafixers.util.Either;
import ky.someone.mods.gag.util.VerticalAlignment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TooltipListBuilder implements TooltipSink {
	private final List<Either<Component, TooltipComponent>> tooltips = new ArrayList<>();

	public TooltipListBuilder add(TooltipComponent component) {
		acceptImage(component);
		return this;
	}

	public TooltipListBuilder text(Component component) {
		acceptText(component);
		return this;
	}

	public TooltipListBuilder text(String text) {
		return text(Component.literal(text));
	}

	public TooltipListBuilder hstack(Consumer<TooltipListBuilder> components) {
		return hstack(components, 0);
	}

	public TooltipListBuilder hstack(Consumer<TooltipListBuilder> components, int padding) {
		var list = new HStackTooltipComponent(padding, VerticalAlignment.TOP);
		components.accept(list);
		return add(list);
	}

	public TooltipListBuilder item(ItemStack item) {
		return add(new ItemTooltipComponent(item));
	}

	public TooltipListBuilder item(ItemLike item) {
		return item(item.asItem().getDefaultInstance());
	}

	public TooltipListBuilder itemWithCount(ItemStack item) {
		return add(new ItemTooltipComponent(item, true, null));
	}

	public TooltipListBuilder itemWithText(ItemStack item, Component text) {
		return add(new ItemTooltipComponent(item, false, text));
	}

	public void build(TooltipSink sink) {
		for (var tooltip : tooltips) {
			tooltip.ifLeft(sink::acceptText).ifRight(sink::acceptImage);
		}
	}

	@Override
	public void acceptText(Component text) {
		tooltips.add(Either.left(text));
	}

	@Override
	public void acceptImage(TooltipComponent image) {
		tooltips.add(Either.right(image));
	}

}
