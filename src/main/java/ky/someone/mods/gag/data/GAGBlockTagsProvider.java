package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.entity.MiningDynamiteEntity;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GAGBlockTagsProvider extends BlockTagsProvider {
	public GAGBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
		super(output, lookup, GAGUtil.MOD_ID, efh);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(MiningDynamiteEntity.MINING_DYNAMITE_EFFECTIVE)
				.addTag(BlockTags.BASE_STONE_OVERWORLD)
				.addTag(BlockTags.BASE_STONE_NETHER)
				.addTag(BlockTags.DIRT)
				.addTag(BlockTags.LUSH_GROUND_REPLACEABLE);

		tag(BlockTags.MINEABLE_WITH_AXE).add(GAGRegistry.NO_SOLICITORS_SIGN.get());
	}
}
