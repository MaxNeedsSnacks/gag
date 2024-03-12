package ky.someone.mods.gag.item;

import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.entity.FishingDynamiteEntity;
import ky.someone.mods.gag.entity.MiningDynamiteEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.UnaryOperator;

public interface ItemRegistry {
	DeferredRegister.Items ITEMS = DeferredRegister.createItems(GAGUtil.MOD_ID);

	DeferredItem<TemporalPouchItem> TIME_SAND_POUCH = ITEMS.register("time_sand_pouch", TemporalPouchItem::new);
	//DeferredItem<TimeSandItem> SANDS_OF_TIME = ITEMS.register("time_sand", TimeSandItem::new);

	DeferredItem<Item> ESCAPE_ROPE = ITEMS.register("escape_rope", EscapeRopeItem::new);
	DeferredItem<Item> HEARTHSTONE = ITEMS.register("hearthstone", () -> new HearthstoneItem());
	DeferredItem<Item> ENERGIZED_HEARTHSTONE = ITEMS.register("energized_hearthstone", EnergizedHearthstoneItem::new);

	DeferredItem<Item> SACRED_SALT = repelling("sacred_salt", p -> p.stacksTo(16).rarity(Rarity.UNCOMMON), 40 * 20, 1, false);
	DeferredItem<Item> SACRED_SALVE = repelling("sacred_salve", p -> p.stacksTo(4).rarity(Rarity.RARE), 120 * 20, 2, true);
	DeferredItem<Item> SACRED_BALM = repelling("sacred_balm", p -> p.stacksTo(4).rarity(Rarity.RARE), 360 * 20, 0, true);

	// TODO: only downward throwing speed should be accelerated
	DeferredItem<Item> MINING_DYNAMITE = dynamite("mining_dynamite", MiningDynamiteEntity::new, List.of(
			Component.translatable("item.gag.mining_dynamite.info").withStyle(GAGUtil.TOOLTIP_MAIN)
	), 1.5);
	DeferredItem<Item> FISHING_DYNAMITE = dynamite("fishing_dynamite", FishingDynamiteEntity::new, List.of(
			Component.translatable("item.gag.fishing_dynamite.info").withStyle(GAGUtil.TOOLTIP_MAIN)
	), 1.5);

	DeferredItem<Item> LABELING_TOOL = ITEMS.register("labeling_tool", LabelingToolItem::new);
	DeferredItem<Item> PIGMENT_JAR = ITEMS.register("pigment_jar", PigmentJarItem::new);

	private static DeferredItem<Item> repelling(String name, UnaryOperator<Item.Properties> properties, int duration, int amplifier, boolean hasTooltip) {
		return ITEMS.register(name, () -> new RepellingItem(properties.apply(new Item.Properties()), duration, amplifier, hasTooltip));
	}

	private static DeferredItem<Item> dynamite(String name, DynamiteItem.EntityFactory factory, List<Component> tooltip, double throwSpeed) {
		return ITEMS.register(name, () -> new DynamiteItem<>(new Item.Properties(), factory, tooltip, throwSpeed));
	}
}
