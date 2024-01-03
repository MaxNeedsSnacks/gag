package ky.someone.mods.gag.recipe.pigment;

import ky.someone.mods.gag.item.ItemRegistry;
import ky.someone.mods.gag.item.PigmentJarItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PigmentJarMixingRecipe extends CustomRecipe {
	public PigmentJarMixingRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		// accept iff there are exactly 2 non-empty pigment jars and nothing else
		int found = 0;

		for (var stack : container.getItems()) {
			if (stack.is(ItemRegistry.PIGMENT_JAR.get()) && !PigmentJarItem.isEmpty(stack)) {
				found++;
			} else if (!stack.isEmpty()) {
				return false;
			}
		}

		return found == 2;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess reg) {
		// mix the contents of the two jars together
		PigmentJarItem.Pigment first = null, second = null;

		for (var stack : container.getItems()) {
			if (stack.is(ItemRegistry.PIGMENT_JAR.get()) && !PigmentJarItem.isEmpty(stack)) {
				if (first == null) {
					first = PigmentJarItem.getPigment(stack);
				} else {
					second = PigmentJarItem.getPigment(stack);
				}
			}
		}

		if (first == null || second == null) return ItemStack.EMPTY;

		return first.mix(second).asJar();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		var list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		// find the first jar and place an empty one at that index
		for (int i = 0; i < container.getContainerSize(); i++) {
			var stack = container.getItem(i);
			if (stack.is(ItemRegistry.PIGMENT_JAR.get()) && PigmentJarItem.isEmpty(stack)) {
				list.set(i, ItemRegistry.PIGMENT_JAR.get().getDefaultInstance());
				return list;
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
		return null;
	}
}
