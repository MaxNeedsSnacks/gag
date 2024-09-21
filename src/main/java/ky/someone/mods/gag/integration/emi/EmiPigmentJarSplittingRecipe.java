package ky.someone.mods.gag.integration.emi;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.item.data.Pigment;
import ky.someone.mods.gag.recipe.pigment.PigmentJarSplittingRecipe;
import net.minecraft.world.item.DyeColor;

import java.util.List;
import java.util.Random;

public class EmiPigmentJarSplittingRecipe extends EmiPatternCraftingRecipe {
	private static final List<DyeColor> DYES = List.of(DyeColor.values());

	public EmiPigmentJarSplittingRecipe() {
		super(List.of(
				EmiStack.of(GAGRegistry.PIGMENT_JAR)
						.setRemainder(EmiStack.of(GAGRegistry.PIGMENT_JAR))
		), EmiStack.of(GAGRegistry.PIGMENT_JAR), PigmentJarSplittingRecipe.ID);
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		return new GeneratedSlotWidget(r -> {
			var pigment = randomPigment(r);
			var amount = pigment.amount();

			if (slot == 0) {
				return EmiStack.of(pigment.asJar());
			} else {
				return EmiStack.EMPTY;
			}
		}, unique, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(this::getJarForRecipe, unique, x, y);
	}

	private EmiStack getJarForRecipe(Random random) {
		var pigment = randomPigment(random);
		var amount = pigment.amount() / 2;

		return EmiStack.of(pigment.withAmount(amount).asJar()).setAmount(2);
	}

	private Pigment randomPigment(Random random) {
		var color = random.nextInt(2 << 24);
		var amount = random.nextInt(PigmentJarItem.MAX_AMOUNT / 4) * 2 + 4;

		return Pigment.ofRgb(color, amount);
	}
}
