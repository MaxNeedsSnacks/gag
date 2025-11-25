package ky.someone.mods.gag.data.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.extensions.ILevelExtension;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class ComponentL10nProvider implements DataProvider {
	private final Map<String, Component> data = new TreeMap<>();
	private final PackOutput output;
	private final String modid;
	private final String locale;

	public ComponentL10nProvider(PackOutput output, String modid, String locale) {
		this.output = output;
		this.modid = modid;
		this.locale = locale;
	}

	protected abstract void addTranslations();

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		addTranslations();

		if (!data.isEmpty())
			return save(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));

		return CompletableFuture.allOf();
	}

	@Override
	public String getName() {
		return "Languages: " + locale + " for mod: " + modid;
	}

	private CompletableFuture<?> save(CachedOutput cache, Path target) {
		JsonObject json = new JsonObject();
		for (var entry : data.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();

			var str = value.tryCollapseToString();
			if (str != null) {
				json.addProperty(key, str);
			} else {
				json.add(key, ComponentSerialization.CODEC
						.encodeStart(JsonOps.INSTANCE, value)
						.map(elem -> Util.make(new JsonArray(), arr -> arr.add(elem)))
						.getOrThrow());
			}
		}

		return DataProvider.saveStable(cache, json, target);
	}

	public void addBlock(Supplier<? extends Block> key, String name) {
		add(key.get(), name);
	}

	public void add(Block key, String name) {
		add(key.getDescriptionId(), name);
	}

	public void addItem(Supplier<? extends Item> key, String name) {
		add(key.get(), name);
	}

	public void add(Item key, String name) {
		add(key.getDescriptionId(), name);
	}

	public void addItemStack(Supplier<ItemStack> key, String name) {
		add(key.get(), name);
	}

	public void add(ItemStack key, String name) {
		add(key.getDescriptionId(), name);
	}

	public void addEffect(Supplier<? extends MobEffect> key, String name) {
		add(key.get(), name);
	}

	public void add(MobEffect key, String name) {
		add(key.getDescriptionId(), name);
	}

	public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
		add(key.get(), name);
	}

	public void add(EntityType<?> key, String name) {
		add(key.getDescriptionId(), name);
	}

	public void addTag(Supplier<? extends TagKey<?>> key, String name) {
		add(key.get(), name);
	}

	public void add(TagKey<?> tagKey, String name) {
		add(Tags.getTagTranslationKey(tagKey), name);
	}

	public void addDimension(ResourceKey<Level> dimension, String value) {
		add(dimension.location().toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), value);
	}

	// component
	public void addBlock(Supplier<? extends Block> key, Component name) {
		add(key.get(), name);
	}

	public void add(Block key, Component name) {
		add(key.getDescriptionId(), name);
	}

	public void addItem(Supplier<? extends Item> key, Component name) {
		add(key.get(), name);
	}

	public void add(Item key, Component name) {
		add(key.getDescriptionId(), name);
	}

	public void addItemStack(Supplier<ItemStack> key, Component name) {
		add(key.get(), name);
	}

	public void add(ItemStack key, Component name) {
		add(key.getDescriptionId(), name);
	}

	public void addEffect(Supplier<? extends MobEffect> key, Component name) {
		add(key.get(), name);
	}

	public void add(MobEffect key, Component name) {
		add(key.getDescriptionId(), name);
	}

	public void addEntityType(Supplier<? extends EntityType<?>> key, Component name) {
		add(key.get(), name);
	}

	public void add(EntityType<?> key, Component name) {
		add(key.getDescriptionId(), name);
	}

	public void addTag(Supplier<? extends TagKey<?>> key, Component name) {
		add(key.get(), name);
	}

	public void add(TagKey<?> tagKey, Component name) {
		add(Tags.getTagTranslationKey(tagKey), name);
	}

	public void add(String key, String value) {
		add(key, Component.literal(value));
	}

	public void add(String key, Component value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate translation key " + key);
	}

	public void addDimension(ResourceKey<Level> dimension, Component value) {
		add(dimension.location().toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), value);
	}

}
