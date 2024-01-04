package ky.someone.mods.gag.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.recipe.pigment.PigmentJarFromDyeRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarLeatherDyingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarSplittingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public interface GAGRecipeSerializers {
	DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.RECIPE_SERIALIZER);

	RegistrySupplier<RecipeSerializer<?>> PIGMENT_JAR_MIXING = special("pigment_jar_mixing", PigmentJarMixingRecipe::new);
	RegistrySupplier<RecipeSerializer<?>> PIGMENT_JAR_FROM_DYE = special("pigment_jar_from_dye", PigmentJarFromDyeRecipe::new);
	RegistrySupplier<RecipeSerializer<?>> PIGMENT_JAR_SPLITTING = special("pigment_jar_splitting", PigmentJarSplittingRecipe::new);
	RegistrySupplier<RecipeSerializer<?>> PIGMENT_JAR_LEATHER_DYING = special("pigment_jar_leather_dying", PigmentJarLeatherDyingRecipe::new);

	private static RegistrySupplier<RecipeSerializer<?>> special(String name, SimpleCraftingRecipeSerializer.Factory<?> factory) {
		return RECIPE_SERIALIZERS.register(name, () -> new SimpleCraftingRecipeSerializer<>(factory));
	}
}
