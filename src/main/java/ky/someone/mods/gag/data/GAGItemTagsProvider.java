package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class GAGItemTagsProvider extends ItemTagsProvider {

	public GAGItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, lookup, blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(ItemTags.DURABILITY_ENCHANTABLE)
				.add(GAGRegistry.HEARTHSTONE.getKey())
				.add(GAGRegistry.ENERGIZED_HEARTHSTONE.getKey())
				.add(GAGRegistry.ESCAPE_ROPE.getKey())
				;
	}
}
