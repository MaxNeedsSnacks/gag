package ky.someone.mods.gag;

import dev.shadowsoffire.placebo.registry.DeferredHelper;
import ky.someone.mods.gag.block.NoSolicitorsSign;
import ky.someone.mods.gag.effect.RepellingEffect;
import ky.someone.mods.gag.entity.FishingDynamiteEntity;
import ky.someone.mods.gag.entity.MiningDynamiteEntity;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.DynamiteItem;
import ky.someone.mods.gag.item.EnergizedHearthstoneItem;
import ky.someone.mods.gag.item.EscapeRopeItem;
import ky.someone.mods.gag.item.HearthstoneItem;
import ky.someone.mods.gag.item.ItemWithSubsets;
import ky.someone.mods.gag.item.LabelingToolItem;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.item.RepellingItem;
import ky.someone.mods.gag.item.TemporalPouchItem;
import ky.someone.mods.gag.item.data.Pigment;
import ky.someone.mods.gag.item.data.TeleportPos;
import ky.someone.mods.gag.menu.LabelingMenu;
import ky.someone.mods.gag.recipe.pigment.PigmentJarFromDyeRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarLeatherDyingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarMixingRecipe;
import ky.someone.mods.gag.recipe.pigment.PigmentJarSplittingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface GAGRegistry {
	// TODO: remove once shadow updates Placebo
	DeferredHelper HELPER = new DeferredHelper(GAGUtil.MOD_ID) {
		@Override
		public <T extends Item> DeferredItem<T> item(String path, Supplier<T> factory) {
			var item = super.item(path, factory);
			ITEMS.add(item);
			return item;
		}
	};

	Collection<DeferredItem<?>> ITEMS = new HashSet<>();

	@SubscribeEvent
	static void register(RegisterEvent event) {
		HELPER.register(event);
	}

	DeferredItem<Item> HEARTHSTONE = HELPER.item("hearthstone", () -> new HearthstoneItem());
	DeferredHolder<CreativeModeTab, ?> CREATIVE_TAB = HELPER.creativeTab("gag", (builder) -> builder
			.icon(() -> HEARTHSTONE.get().getDefaultInstance())
			.title(Component.literal("Gadgets against Grind"))
			.displayItems((params, output) -> ITEMS.stream()
					.map(Holder::value)
					.<ItemStack>mapMulti((item, sink) -> {
						sink.accept(item.getDefaultInstance());
						if (item instanceof ItemWithSubsets subsets) {
							for (var subItem : subsets.getAdditionalSubItems()) {
								sink.accept(subItem);
							}
						}
					})
					.forEach(output::accept)));

	BlockAndItem<NoSolicitorsSign, ?> NO_SOLICITORS_SIGN = BlockAndItem.create("no_solicitors", NoSolicitorsSign::new);

	// items
	DeferredItem<TemporalPouchItem> TIME_SAND_POUCH = HELPER.item("time_sand_pouch", TemporalPouchItem::new);
	//DeferredItem<TimeSandItem> SANDS_OF_TIME = ITEMS.register("time_sand", TimeSandItem::new);

	DeferredItem<Item> ESCAPE_ROPE = HELPER.item("escape_rope", EscapeRopeItem::new);

	DeferredItem<Item> ENERGIZED_HEARTHSTONE = HELPER.item("energized_hearthstone", EnergizedHearthstoneItem::new);

	DeferredItem<Item> SACRED_SALT = repelling("sacred_salt", p -> p.stacksTo(16).rarity(Rarity.UNCOMMON), 40 * 20, 1, false);
	DeferredItem<Item> SACRED_SALVE = repelling("sacred_salve", p -> p.stacksTo(4).rarity(Rarity.RARE), 120 * 20, 2, true);
	DeferredItem<Item> SACRED_BALM = repelling("sacred_balm", p -> p.stacksTo(4).rarity(Rarity.RARE), 360 * 20, 0, true);

	// TODO: only downward throwing speed should be accelerated
	DeferredItem<Item> MINING_DYNAMITE_ITEM = dynamite("mining_dynamite", MiningDynamiteEntity::new, List.of(
			Component.translatable("item.gag.mining_dynamite.info").withStyle(GAGUtil.TOOLTIP_MAIN)
	), 1.5);
	DeferredItem<Item> FISHING_DYNAMITE_ITEM = dynamite("fishing_dynamite", FishingDynamiteEntity::new, List.of(
			Component.translatable("item.gag.fishing_dynamite.info").withStyle(GAGUtil.TOOLTIP_MAIN)
	), 1.5);

	DeferredItem<Item> LABELING_TOOL = HELPER.item("labeling_tool", LabelingToolItem::new);
	DeferredItem<Item> PIGMENT_JAR = HELPER.item("pigment_jar", PigmentJarItem::new);

	static DeferredItem<Item> repelling(String name, UnaryOperator<Item.Properties> properties, int duration, int amplifier, boolean hasTooltip) {
		return HELPER.item(name, () -> new RepellingItem(properties.apply(new Item.Properties()), duration, amplifier, hasTooltip));
	}

	static DeferredItem<Item> dynamite(String name, DynamiteItem.EntityFactory factory, List<Component> tooltip, double throwSpeed) {
		return HELPER.item(name, () -> new DynamiteItem<>(new Item.Properties(), factory, tooltip, throwSpeed));
	}

	// data components
	DataComponentType<Pigment> PIGMENT_DATA = HELPER.component("pigment", builder -> builder
			.persistent(Pigment.CODEC)
			.networkSynchronized(Pigment.STREAM_CODEC)
	);

	DataComponentType<Integer> GRAINS_OF_TIME_DATA = HELPER.component("grains_of_time", builder -> builder
			.persistent(ExtraCodecs.POSITIVE_INT)
			.networkSynchronized(ByteBufCodecs.VAR_INT)
	);

	DataComponentType<TeleportPos> TELEPORT_TARGET_DATA = HELPER.component("teleport_target", builder -> builder
			.persistent(TeleportPos.CODEC)
			.networkSynchronized(TeleportPos.STREAM_CODEC)
	);

	Supplier<EntityType<TimeAcceleratorEntity>> TIME_ACCELERATOR =
			HELPER.entity("time_accelerator", TimeAcceleratorEntity::new, MobCategory.MISC, builder -> builder
					.sized(0.1f, 0.1f)
					.noSummon());    // no serialisation means no data fixers

	Supplier<EntityType<MiningDynamiteEntity>> MINING_DYNAMITE =
			HELPER.entity("mining_dynamite", MiningDynamiteEntity::new, MobCategory.MISC, builder -> builder
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10));

	Supplier<EntityType<FishingDynamiteEntity>> FISHING_DYNAMITE =
			HELPER.entity("fishing_dynamite", FishingDynamiteEntity::new, MobCategory.MISC, builder -> builder
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10));
	// sounds
	Supplier<SoundEvent> DYNAMITE_THROW = simpleSound("entity.dynamite.throw");
	Supplier<SoundEvent> HEARTHSTONE_THUNDER = simpleSound("item.hearthstone.thunder");
	Supplier<SoundEvent> REPELLING_APPLY = simpleSound("item.repelling.apply");
	Supplier<SoundEvent> TELEPORT = simpleSound("generic.teleport");

	Supplier<SoundEvent> TELEPORT_FAIL = simpleSound("generic.teleport.fail");

	static Supplier<SoundEvent> simpleSound(String name) {
		return HELPER.sound(name, () -> SoundEvent.createVariableRangeEvent(GAGUtil.id(name)));
	}

	// recipe serialisers
	DeferredHolder<RecipeSerializer<?>, ?> PIGMENT_JAR_MIXING = specialRecipe("pigment_jar_mixing", PigmentJarMixingRecipe::new);
	DeferredHolder<RecipeSerializer<?>, ?> PIGMENT_JAR_FROM_DYE = specialRecipe("pigment_jar_from_dye", PigmentJarFromDyeRecipe::new);
	DeferredHolder<RecipeSerializer<?>, ?> PIGMENT_JAR_SPLITTING = specialRecipe("pigment_jar_splitting", PigmentJarSplittingRecipe::new);

	DeferredHolder<RecipeSerializer<?>, ?> PIGMENT_JAR_LEATHER_DYING = specialRecipe("pigment_jar_leather_dying", PigmentJarLeatherDyingRecipe::new);

	static DeferredHolder<RecipeSerializer<?>, ?> specialRecipe(String name, SimpleCraftingRecipeSerializer.Factory<?> factory) {
		return HELPER.recipeSerializer(name, () -> new SimpleCraftingRecipeSerializer<>(factory));
	}

	// menus
	DeferredHolder<MenuType<?>, MenuType<LabelingMenu>> LABELING_MENU = HELPER.menu("labeling", LabelingMenu::new);

	// particles
	DeferredHolder<ParticleType<?>, SimpleParticleType> MAGIC_PARTICLE = HELPER.simpleParticle("magic", true);

	// effects
	DeferredHolder<MobEffect, RepellingEffect> REPELLING = HELPER.effect("repelling", RepellingEffect::new);


	record BlockAndItem<B extends Block, I extends Item>(
			DeferredBlock<B> block,
			DeferredItem<I> item
	) implements Supplier<B>, ItemLike {
		public static <B extends Block> BlockAndItem<B, BlockItem> create(String name, Supplier<B> supplier) {
			var block = HELPER.block(name, supplier);
			var item = HELPER.blockItem(name, block);
			return new BlockAndItem<>(block, item);
		}

		@Override
		public B get() {
			return block.get();
		}

		@Override
		public Item asItem() {
			return item.asItem();
		}
	}
}
