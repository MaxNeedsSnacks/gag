package ky.someone.mods.gag.recipe.pigment;

import ky.someone.mods.gag.item.ItemRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.recipe.GAGRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PigmentJarSplittingRecipe extends CustomRecipe {
	public PigmentJarSplittingRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		// accept if there is exactly one filled and one empty jar
		var filled = false;
		var empty = false;

		for (var stack : container.getItems()) {
			if (stack.is(ItemRegistry.PIGMENT_JAR.get())) {
				if (PigmentJarItem.isEmpty(stack)) {
					if (empty) return false;
					empty = true;
				} else {
					if (filled) return false;
					filled = true;
				}
			}
		}

		return filled && empty;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess reg) {
		// find the filled jar, and return two jars with half the pigment each (rounded down)
		for (var stack : container.getItems()) {
			if (stack.is(ItemRegistry.PIGMENT_JAR.get())) {
				var pigment = PigmentJarItem.getPigment(stack);
				if (pigment != null && !pigment.isEmpty()) {
					var newStack = pigment.withAmount(pigment.amount() / 2);
					return newStack.asJar().copyWithCount(2);
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int j) {
		return i * j >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return GAGRecipeSerializers.PIGMENT_JAR_SPLITTING.get();
	}
}
