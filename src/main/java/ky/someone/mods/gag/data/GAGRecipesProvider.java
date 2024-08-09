package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.recipe.pigment.PigmentJarFromDyeRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarLeatherDyingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarSplittingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeBuilder.getDefaultRecipeId;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.data.recipes.SpecialRecipeBuilder.special;

public class GAGRecipesProvider extends RecipeProvider {
	public GAGRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		shaped(RecipeCategory.MISC, GAGRegistry.ESCAPE_ROPE)
				.pattern("#T").pattern("# ").pattern("#L")
				.define('#', Items.CHAIN)
				.define('T', Items.TRIPWIRE_HOOK)
				.define('L', Items.LEAD)
				.unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
				.save(output);

		shaped(RecipeCategory.MISC, GAGRegistry.FISHING_DYNAMITE_ITEM)
				.pattern("GKG").pattern("KTK").pattern("GFG")
				.define('G', Ingredient.of(Items.GRAVEL, Items.SAND))
				.define('K', Items.DRIED_KELP)
				.define('T', Items.TNT)
				.define('F', Items.FISHING_ROD)
				.unlockedBy("has_tnt", has(Items.TNT))
				.save(output);

		shaped(RecipeCategory.MISC, GAGRegistry.HEARTHSTONE)
				.pattern(" D ").pattern("DAD").pattern(" D ")
				.define('D', Items.POLISHED_DEEPSLATE)
				.define('A', Items.AMETHYST_SHARD)
				.unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
				.save(output);

		shaped(RecipeCategory.MISC, GAGRegistry.LABELING_TOOL)
				.pattern(" ##").pattern("#SI").pattern(" IC")
				.define('#', Items.PAPER)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', Tags.Items.RODS_WOODEN)
				.define('C', Items.CHAIN)
				.unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
				.save(output);

		shaped(RecipeCategory.MISC, GAGRegistry.MINING_DYNAMITE_ITEM)
				.pattern("DDD").pattern("DTD").pattern("DFD")
				.define('D', Tags.Items.COBBLESTONES_DEEPSLATE)
				.define('T', Items.TNT)
				.define('F', Items.FIREWORK_ROCKET)
				.unlockedBy("has_tnt", has(Items.TNT))
				.save(output);

		shapeless(RecipeCategory.MISC, GAGRegistry.NO_SOLICITORS_SIGN)
				.requires(ItemTags.SIGNS)
				.requires(Items.EMERALD)
				.requires(Items.FLINT)
				.unlockedBy("has_emerald", has(Items.EMERALD))
				.save(output);

		shapeless(RecipeCategory.MISC, GAGRegistry.PIGMENT_JAR)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLOWSTONE_DUST)
				.requires(ItemTags.WOODEN_BUTTONS)
				.unlockedBy("has_glowstone", has(Items.GLOWSTONE_DUST))
				.save(output);

		special(PigmentJarMixingRecipe::new).save(output, GAGUtil.id("pigment_jar_mixing"));
		special(PigmentJarFromDyeRecipe::new).save(output, GAGUtil.id("pigment_jar_from_dye"));
		special(PigmentJarLeatherDyingRecipe::new).save(output, GAGUtil.id("pigment_jar_leather_dying"));
		special(PigmentJarSplittingRecipe::new).save(output, GAGUtil.id("pigment_jar_splitting"));

		shapeless(RecipeCategory.MISC, Items.GLASS_BOTTLE)
				.requires(GAGRegistry.PIGMENT_JAR)
				.unlockedBy("has_pigment", has(GAGRegistry.PIGMENT_JAR))
				.save(output, GAGUtil.id("pigment_jar_to_regular_bottle"));

		shapeless(RecipeCategory.MISC, GAGRegistry.SACRED_BALM)
				.requires(Items.HONEYCOMB)
				.requires(Items.PHANTOM_MEMBRANE)
				.requires(GAGRegistry.SACRED_SALT, 4)
				.requires(Items.BOWL)
				.unlockedBy("has_honey", has(Items.HONEYCOMB))
				.save(output);

		shapeless(RecipeCategory.MISC, GAGRegistry.SACRED_BALM)
				.requires(Items.PHANTOM_MEMBRANE)
				.requires(GAGRegistry.SACRED_SALVE)
				.requires(Items.BOWL)
				.unlockedBy("has_salve", has(GAGRegistry.SACRED_SALVE))
				.save(output, getDefaultRecipeId(GAGRegistry.SACRED_BALM).withSuffix("_from_salve"));

		shapeless(RecipeCategory.MISC, GAGRegistry.SACRED_SALT, 4)
				.requires(Ingredient.of(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST))
				.requires(Ingredient.of(Items.FERMENTED_SPIDER_EYE, Items.POISONOUS_POTATO))
				.requires(Ingredient.of(Items.GLOW_BERRIES))
				.unlockedBy("has_glow_berries", has(Items.GLOW_BERRIES))
				.save(output);

		shapeless(RecipeCategory.MISC, GAGRegistry.SACRED_SALVE)
				.requires(Items.GLOW_INK_SAC)
				.requires(Items.HONEY_BOTTLE)
				.requires(ItemTags.SMALL_FLOWERS)
				.requires(GAGRegistry.SACRED_SALT, 3)
				.unlockedBy("has_salt", has(GAGRegistry.SACRED_SALT))
				.save(output);

		shaped(RecipeCategory.MISC, GAGRegistry.TIME_SAND_POUCH)
				.pattern("GNG").pattern("LEL").pattern("LLL")
				.define('G', Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
				.define('N', Items.NAUTILUS_SHELL)
				.define('E', Items.ENDER_EYE)
				.define('L', Items.LEATHER)
				.unlockedBy("has_nautilus", has(Items.NAUTILUS_SHELL))
				.save(output);
	}
}
