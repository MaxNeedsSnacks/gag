package ky.someone.mods.gag.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GAGLootTableProvider extends LootTableProvider {
	public GAGLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Set.of(), List.of(
				new LootTableProvider.SubProviderEntry(GAGBlockLoot::new, LootContextParamSets.BLOCK)
		), registries);
	}
}
