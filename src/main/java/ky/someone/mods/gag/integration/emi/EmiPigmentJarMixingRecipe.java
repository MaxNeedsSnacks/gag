package ky.someone.mods.gag.integration.emi;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.item.data.Pigment;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import net.minecraft.world.item.DyeColor;

import java.util.List;
import java.util.Random;

public class EmiPigmentJarMixingRecipe extends EmiPatternCraftingRecipe {
	private static final List<DyeColor> DYES = List.of(DyeColor.values());

	public EmiPigmentJarMixingRecipe() {
		super(List.of(
				EmiStack.of(GAGRegistry.PIGMENT_JAR),
				EmiStack.of(GAGRegistry.PIGMENT_JAR)
		), EmiStack.of(GAGRegistry.PIGMENT_JAR), PigmentJarMixingRecipe.ID);
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		return new GeneratedSlotWidget(r -> {
			var first = randomPigment(r).asJar();
			var second = randomPigment(r).asJar();

			return switch (slot) {
				case 0 -> EmiStack.of(first);
				case 1 -> EmiStack.of(second);
				default -> EmiStack.EMPTY;
			};
		}, unique, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(this::getJarForRecipe, unique, x, y);
	}

	private EmiStack getJarForRecipe(Random random) {
		var first = randomPigment(random);
		var second = randomPigment(random);

		return EmiStack.of(first.mix(second).asJar());
	}

	private Pigment randomPigment(Random random) {
		var color = random.nextInt(2 << 24);
		var amount = random.nextInt(PigmentJarItem.MAX_AMOUNT / 2) + 1;

		return Pigment.ofRgb(color, amount);
	}
}
