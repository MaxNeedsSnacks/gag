package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.data.provider.ComponentL10nProvider;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class GAGLangProvider extends ComponentL10nProvider {
	public GAGLangProvider(PackOutput output) {
		super(output, GAGUtil.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.gag.gag", "GAG — Gadgets Against Grind");

		add("info.gag.repelling_item", "When applied, gives the player an effect that stops nearby hostile mobs from spawning.");
		add("info.gag.supports_unbreaking", "(can be enchanted with Unbreaking and Mending)");
		add("info.gag.lightning_crafting_hint", "Strike with Lightning");

		// add("item.gag.time_sand", "Sands of Time");
		addItem(GAGRegistry.TIME_SAND_POUCH, "Temporal Pouch");

		addItemSub(GAGRegistry.TIME_SAND_POUCH, "flavour", Component.literal("If I could save time in a ")
				.append(Component.literal("bottle").withStyle(ChatFormatting.STRIKETHROUGH))
				.append(Component.literal(" bundle...")));
		addItemInfo(GAGRegistry.TIME_SAND_POUCH, "stored_grains", "Contains %2$s worth of Grains of Time");
		addItemInfo(GAGRegistry.TIME_SAND_POUCH, "Slowly accumulates time while in a player's inventory. Use on a ticking block to accelerate it, using some of the stored time from the pouch.");

		addItem(GAGRegistry.ESCAPE_ROPE, "Escape Rope");
		addItemSub(GAGRegistry.ESCAPE_ROPE, "no_space", "Could not find any space on the surface to teleport to.");
		addItemInfo(GAGRegistry.ESCAPE_ROPE, "Teleports a player out of caves and buildings to the nearest point on the surface.");

		addItem(GAGRegistry.HEARTHSTONE, "Hearthstone");
		addItem(GAGRegistry.ENERGIZED_HEARTHSTONE, "Energized Hearthstone");

		addItemSub(GAGRegistry.HEARTHSTONE, "no_target", "Couldn't find anywhere to teleport to, did you set your spawn point?");
		addItemSub(GAGRegistry.HEARTHSTONE, "too_weak", "It seems this hearthstone is too weak to call home from this place.");
		addItemSub(GAGRegistry.HEARTHSTONE, "info", "Teleports a player to their personal spawn point, or, if enabled, the world spawn.");
		addItemSub(GAGRegistry.HEARTHSTONE, "info_adv", "A stronger hearthstone that has channelled the energy of raw lightning, and can be used to teleport to a single bound location. You can reset the stone by hitting it with lightning again.");
		addItemSub(GAGRegistry.HEARTHSTONE, "target.bound", "\uD83E\uDDED %s");
		addItemSub(GAGRegistry.HEARTHSTONE, "target.hidden", "[hidden target]");
		addItemSub(GAGRegistry.HEARTHSTONE, "target.unbound", "\uD83E\uDDED Unbound");
		addItemSub(GAGRegistry.HEARTHSTONE, "target.respawn", "Your Respawn Point");
		addItemSub(GAGRegistry.HEARTHSTONE, "warmup", "Teleporting in %s seconds...");

		addItem(GAGRegistry.LABELING_TOOL, "Labeling Tool");
		addItemInfo(GAGRegistry.LABELING_TOOL, "Used to change the name of any item in your inventory at no cost.");
		addItemSub(GAGRegistry.LABELING_TOOL, "text_box", "Rename Item");

		addItem(GAGRegistry.PIGMENT_JAR, "Pigment Jar");
		addItemSub(GAGRegistry.PIGMENT_JAR, "contents", "Contains %s charges of %s");
		addItemSub(GAGRegistry.PIGMENT_JAR, "contents.empty", "Empty");
		addItemInfo(GAGRegistry.PIGMENT_JAR, "empty.1", "An empty jar used to store pigments. Probably needs to be filled to be useful.");
		addItemInfo(GAGRegistry.PIGMENT_JAR, "empty.2", "Hint: Grind up some dye with flint, then add some milk.");
		addItemInfo(GAGRegistry.PIGMENT_JAR, "filled.1", "Can be used in a labeling tool to change the color of the item's name.");
		addItemInfo(GAGRegistry.PIGMENT_JAR, "filled.2", "Hint: Combine with other pigment jars to mix colors.");

		addBlock(GAGRegistry.NO_SOLICITORS_SIGN, "'No Solicitors!' Sign");
		addBlockInfo(GAGRegistry.NO_SOLICITORS_SIGN, "1", "Wards off certain pesky intruders within a reasonable area.");
		addBlockInfo(GAGRegistry.NO_SOLICITORS_SIGN, "2", "(Right-click with wool to toggle silent mode)");
		addBlockSub(GAGRegistry.NO_SOLICITORS_SIGN, "silent", "Silent Mode: %s");

		addItem(GAGRegistry.SACRED_SALT, "Sacred Salt");
		addItem(GAGRegistry.SACRED_SALVE, "Sacred Salve");
		addItem(GAGRegistry.SACRED_BALM, "Sacred Balm");
		addItemSub(GAGRegistry.SACRED_SALVE, "extra", "Lasts a bit longer than the salt, and also works in a larger area.");
		addItemSub(GAGRegistry.SACRED_BALM, "extra", "Lasts considerably longer than either the salt or salve, but has a reduced radius.");
		addEffect(GAGRegistry.REPELLING, "Repelling");

		addItem(GAGRegistry.MINING_DYNAMITE_ITEM, "Mining Dynamite");
		addItemInfo(GAGRegistry.MINING_DYNAMITE_ITEM, "Throwable explosive that has a high efficiency against stone. Drops all blocks it destroys.");

		addItem(GAGRegistry.FISHING_DYNAMITE_ITEM, "Fishing Dynamite");
		addItemInfo(GAGRegistry.FISHING_DYNAMITE_ITEM, "Throwable explosive that only works underwater and deals increased damage to fish. Perfect if you don't like fishing!");

		add("commands.gag.give_time.success", "Successfully gave %s ticks worth of time to %s");
		add("commands.gag.give_time.no_pouch", "%s does not have a temporal pouch in their inventory!");

		add("subtitles.gag.entity.dynamite.throw", "Dynamite thrown");
		add("subtitles.gag.item.repelling.apply", "Repelling item applied");

		add("subtitles.gag.item.time_sand_pouch.ding", "Block tick accelerated");
		add("subtitles.gag.item.time_sand_pouch.dong", "Block tick accelerated to very high speed");

		add("features.gag.capability_proxy", "Capability Proxies (GAG)");
		addBlock(GAGRegistry.ITEM_PROXY, "Item Proxy");
		addBlock(GAGRegistry.FLUID_PROXY, "Fluid Proxy");
	}

	public void addItemSub(Supplier<? extends Item> item, String sub, String value) {
		addItemSub(item, sub, Component.literal(value));
	}

	public void addItemSub(Supplier<? extends Item> item, String sub, Component value) {
		var key = item.get().getDescriptionId() + "." + sub;
		add(key, value);
	}

	public void addBlockSub(Supplier<? extends Block> block, String sub, String value) {
		addBlockSub(block, sub, Component.literal(value));
	}

	public void addBlockSub(Supplier<? extends Block> block, String sub, Component value) {
		var key = block.get().getDescriptionId() + "." + sub;
		add(key, value);
	}

	public void addItemInfo(Supplier<? extends Item> item, String key, String value) {
		addItemSub(item, ("info." + key), value);
	}

	public void addItemInfo(Supplier<? extends Item> item, String key, Component value) {
		addItemSub(item, ("info." + key), value);
	}

	public void addItemInfo(Supplier<? extends Item> item, String value) {
		addItemSub(item, "info", value);
	}

	public void addItemInfo(Supplier<? extends Item> item, Component value) {
		addItemSub(item, "info", value);
	}

	public void addBlockInfo(Supplier<? extends Block> block, String key, String value) {
		addBlockSub(block, ("info." + key), value);
	}

	public void addBlockInfo(Supplier<? extends Block> block, String key, Component value) {
		addBlockSub(block, ("info." + key), value);
	}

	public void addBlockInfo(Supplier<? extends Block> block, String value) {
		addBlockSub(block, "info", value);
	}

	public void addBlockInfo(Supplier<? extends Block> block, Component value) {
		addBlockSub(block, "info", value);
	}
}
