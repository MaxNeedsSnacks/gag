package ky.someone.mods.gag.client.tooltip;

import ky.someone.mods.gag.util.GAGUtil;
import ky.someone.mods.gag.util.VerticalAlignment;
import ky.someone.mods.gag.util.tooltip.ItemTooltipComponent;
import ky.someone.mods.gag.util.tooltip.TooltipSink;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ClientTooltipBuilder implements TooltipSink {
	private List<ClientTooltipComponent> lines = new ArrayList<>();

	static final ResourceLocation GENERIC_ARROW = GAGUtil.id("textures/gui/generic_arrow.png");

	private static final ClientTooltipComponent ARROW = new ImageClientTooltip(GENERIC_ARROW, 0, 0, 24, 18, 24, 18, 24, 54);
	private static final ClientTooltipComponent ARROW_EMPTY = new ImageClientTooltip(GENERIC_ARROW, 0, 18, 24, 18, 24, 18, 24, 54);
	private static final ClientTooltipComponent ARROW_FAIL = new ImageClientTooltip(GENERIC_ARROW, 0, 36, 24, 18, 24, 18, 24, 54);

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
		return hstack(components, 0, VerticalAlignment.TOP);
	}

	public ClientTooltipBuilder hstack(List<ClientTooltipComponent> components, int padding, VerticalAlignment alignment) {
		return add(new HStackClientTooltip(components, padding, alignment));
	}

	public ClientTooltipBuilder item(ItemTooltipComponent data) {
		return add(new ItemClientTooltip(data));
	}

	public ClientTooltipBuilder item(ItemStack item) {
		return item(new ItemTooltipComponent(item));
	}

	public ClientTooltipBuilder item(ItemLike item) {
		return item(item.asItem().getDefaultInstance());
	}

	public ClientTooltipBuilder arrow() {
		return add(ARROW);
	}

	public ClientTooltipBuilder emptyArrow() {
		return add(ARROW_EMPTY);
	}

	public ClientTooltipBuilder failArrow() {
		return add(ARROW_FAIL);
	}

	public List<ClientTooltipComponent> build() {
		var list = lines;
		lines = new ArrayList<>();
		return list;
	}

	@Override
	public void acceptText(Component text) {
		text(text);
	}

	@Override
	public void acceptImage(TooltipComponent image) {
		add(image);
	}
}
