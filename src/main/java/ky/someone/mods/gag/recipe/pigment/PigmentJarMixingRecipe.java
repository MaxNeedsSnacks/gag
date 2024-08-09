package ky.someone.mods.gag.recipe.pigment;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.item.data.Pigment;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PigmentJarMixingRecipe extends CustomRecipe {
	public PigmentJarMixingRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		// accept iff there are exactly 2 non-empty pigment jars and nothing else
		int found = 0;

		for (var stack : container.items()) {
			if (stack.is(GAGRegistry.PIGMENT_JAR.get())) {
				if (!PigmentJarItem.isEmpty(stack)) {
					found++;
				}
			} else if (!stack.isEmpty()) {
				return false;
			}
		}

		return found >= 2;
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider reg) {
		// mix the contents of the two jars together
		Pigment result = null;
		for (var stack : container.items()) {
			if (PigmentJarItem.isNonEmptyJar(stack)) {
				var pigment = PigmentJarItem.getPigment(stack);
				if (result == null) {
					result = pigment;
				} else {
					result = result.mix(pigment);
				}
			}
		}

		if (result == null) return ItemStack.EMPTY;
		return result.asJar();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
		var list = NonNullList.withSize(container.size(), ItemStack.EMPTY);

		var first = true;
		for (int i = 0; i < container.size(); i++) {
			var stack = container.getItem(i);
			if (stack.is(GAGRegistry.PIGMENT_JAR.get())) {
				if (first) {
					first = false;
				} else {
					list.set(i, GAGRegistry.PIGMENT_JAR.get().getDefaultInstance());
				}
			}
		}

		return list;
	}

	@Override
	public boolean canCraftInDimensions(int i, int j) {
		return i * j >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return GAGRegistry.PIGMENT_JAR_MIXING.get();
	}
}
