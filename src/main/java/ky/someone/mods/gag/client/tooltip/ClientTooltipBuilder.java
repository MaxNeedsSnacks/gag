package ky.someone.mods.gag.client.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ClientTooltipBuilder {
	private List<ClientTooltipComponent> lines = new ArrayList<>();

	public ClientTooltipBuilder add(ClientTooltipComponent component) {
		lines.add(component);
		return this;
	}

	public ClientTooltipBuilder add(TooltipComponent component) {
		return add(ClientTooltipComponent.create(component));
	}

	public ClientTooltipBuilder text(FormattedCharSequence text) {
		return add(ClientTooltipComponent.create(text));
	}

	public ClientTooltipBuilder text(Component component) {
		return text(component.getVisualOrderText());
	}

	public ClientTooltipBuilder text(String text) {
		return text(Component.literal(text));
	}

	public ClientTooltipBuilder hstack(List<ClientTooltipComponent> components) {
		return hstack(components, 0);
	}

	public ClientTooltipBuilder hstack(List<ClientTooltipComponent> components, int padding) {
		return add(new HStackTooltipComponent(components, padding));
	}

	public ClientTooltipBuilder item(ItemStack item) {
		return add(new ItemTooltipComponent(item));
	}

	public ClientTooltipBuilder item(ItemLike item) {
		return item(item.asItem().getDefaultInstance());
	}

	public ClientTooltipBuilder itemWithText(ItemStack item, Component text) {
		return add(new ItemWithTextTooltipComponent(item, text));
	}

	public ClientTooltipBuilder itemWithText(ItemLike item, Component text) {
		return add(new ItemWithTextTooltipComponent(item.asItem().getDefaultInstance(), text));
	}

	public List<ClientTooltipComponent> build() {
		var list = lines;
		lines = new ArrayList<>();
		return list;
	}
}
