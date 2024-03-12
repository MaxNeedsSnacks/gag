package ky.someone.mods.gag.recipe;


import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.recipe.pigment.PigmentJarFromDyeRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarLeatherDyingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarSplittingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface GAGRecipeSerializers {
	DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, GAGUtil.MOD_ID);

	Supplier<RecipeSerializer<?>> PIGMENT_JAR_MIXING = special("pigment_jar_mixing", PigmentJarMixingRecipe::new);
	Supplier<RecipeSerializer<?>> PIGMENT_JAR_FROM_DYE = special("pigment_jar_from_dye", PigmentJarFromDyeRecipe::new);
	Supplier<RecipeSerializer<?>> PIGMENT_JAR_SPLITTING = special("pigment_jar_splitting", PigmentJarSplittingRecipe::new);
	Supplier<RecipeSerializer<?>> PIGMENT_JAR_LEATHER_DYING = special("pigment_jar_leather_dying", PigmentJarLeatherDyingRecipe::new);

	private static Supplier<RecipeSerializer<?>> special(String name, SimpleCraftingRecipeSerializer.Factory<?> factory) {
		return RECIPE_SERIALIZERS.register(name, () -> new SimpleCraftingRecipeSerializer<>(factory));
	}
}
