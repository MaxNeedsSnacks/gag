package ky.someone.mods.gag.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public interface GAGRecipeSerializers {
	DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.RECIPE_SERIALIZER);

	RegistrySupplier<RecipeSerializer<?>> PIGMENT_JAR_MIXING = special("pigment_jar_mixing", PigmentJarMixingRecipe::new);

	private static RegistrySupplier<RecipeSerializer<?>> special(String name, SimpleCraftingRecipeSerializer.Factory<?> factory) {
		return RECIPE_SERIALIZERS.register(name, () -> new SimpleCraftingRecipeSerializer<>(factory));
	}
}
