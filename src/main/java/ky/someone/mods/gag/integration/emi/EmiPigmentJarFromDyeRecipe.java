package ky.someone.mods.gag.integration.emi;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.item.data.Pigment;
import ky.someone.mods.gag.recipe.pigment.PigmentJarFromDyeRecipe;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Random;

public class EmiPigmentJarFromDyeRecipe extends EmiPatternCraftingRecipe {
	private static final List<DyeColor> DYES = List.of(DyeColor.values());

	public EmiPigmentJarFromDyeRecipe() {
		super(List.of(
				EmiStack.of(GAGRegistry.PIGMENT_JAR),
				EmiStack.of(Items.FLINT),
				EmiStack.of(Items.MILK_BUCKET)
		), EmiStack.of(GAGRegistry.PIGMENT_JAR), PigmentJarFromDyeRecipe.ID);
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		return switch (slot) {
			case 0 -> new SlotWidget(EmiStack.of(Pigment.EMPTY.asJar()), x, y);
			case 1 -> new SlotWidget(EmiStack.of(Items.FLINT), x, y);
			case 2 -> new SlotWidget(EmiStack.of(Items.MILK_BUCKET), x, y);
			default -> {
				final int s = slot - 3;
				yield new GeneratedSlotWidget(r -> {
					var color = DYES.get(r.nextInt(DYES.size()));
					var amount = r.nextInt(4) + 1;

					if (s < amount) {
						return EmiStack.of(DyeItem.byColor(color));
					}
					return EmiStack.EMPTY;
				}, unique, x, y);
			}
		};
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(this::getJarForRecipe, unique, x, y);
	}

	private EmiStack getJarForRecipe(Random random) {
		var color = DYES.get(random.nextInt(DYES.size()));
		var amount = random.nextInt(4) + 1;

		return EmiStack.of(Pigment.forText(color).withAmount(amount * PigmentJarItem.DYE_AMOUNT).asJar());
	}
}
