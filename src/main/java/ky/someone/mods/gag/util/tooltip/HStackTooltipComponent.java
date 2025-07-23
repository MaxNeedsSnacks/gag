package ky.someone.mods.gag.util.tooltip;

import ky.someone.mods.gag.util.VerticalAlignment;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class HStackTooltipComponent extends TooltipListBuilder implements TooltipComponent {
	public final int padding;
	public final VerticalAlignment alignment;

	public HStackTooltipComponent(int padding) {
		this(padding, VerticalAlignment.CENTER);
	}

	public HStackTooltipComponent(int padding, VerticalAlignment alignment) {
		this.padding = padding;
		this.alignment = alignment;
	}
}
