package ky.someone.mods.gag.data.loot;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class GAGBlockLoot extends BlockLootSubProvider {
	protected GAGBlockLoot(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	// empty for now
	private final Map<Block, Function<Block, LootTable.Builder>> specialLoot = Map.of();

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK
				.stream()
				.filter(entry -> entry.getLootTable().location().getNamespace().equals(GAGUtil.MOD_ID))
				.toList();
	}

	@Override
	protected void generate() {
		for (var block : getKnownBlocks()) {
			if (specialLoot.containsKey(block)) {
				add(block, specialLoot.get(block));
			} else {
				dropSelf(block);
			}
		}
	}
}
