package ky.someone.mods.gag.util.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Consumer;

public interface TooltipSink {

	void acceptText(Component text);

	default void acceptText(String text) {
		acceptText(Component.literal(text));
	}

	void acceptImage(TooltipComponent image);

	static TooltipSink of(Consumer<Component> textSink, Consumer<TooltipComponent> imageSink) {
		return new TooltipSink() {
			@Override
			public void acceptText(Component text) {
				textSink.accept(text);
			}

			@Override
			public void acceptImage(TooltipComponent image) {
				imageSink.accept(image);
			}
		};
	}
}
