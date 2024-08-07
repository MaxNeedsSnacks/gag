package ky.someone.mods.gag.world;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GAGPointOfInterestStorage extends SavedData {

	private static final String FILE_NAME = "gag_poi_info";

	private final Map<BlockPos, Holder<Block>> pois = new HashMap<>();

	public static GAGPointOfInterestStorage get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(new Factory<>(GAGPointOfInterestStorage::new, GAGPointOfInterestStorage::load), FILE_NAME);
	}

	public Holder<Block> add(BlockPos pos, Holder<Block> block) {
		setDirty();
		return pois.put(pos, block);
	}

	public boolean removeIfPresent(BlockPos pos) {
		setDirty();
		return pois.remove(pos) != null;
	}

	public Optional<BlockPos> checkNearbyPOIs(Holder<Block> block, BlockPos pos, int radius) {
		for (var poi : pois.entrySet()) {
			if (poi.getValue().is(block) && poi.getKey().distSqr(pos) < radius * radius) {
				return Optional.of(poi.getKey());
			}
		}
		return Optional.empty();
	}

	public static GAGPointOfInterestStorage load(CompoundTag nbt, HolderLookup.Provider registries) {
		var storage = new GAGPointOfInterestStorage();
		if (nbt.contains("pois")) {
			ListTag list = nbt.getList("pois", Tag.TAG_COMPOUND);
			for (var tag : list) {
				var poiTag = (CompoundTag) tag;
				var block = registries.holderOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(poiTag.getString("block"))));
				var pos = BlockPos.of(poiTag.getLong("pos"));
				storage.pois.put(pos, block);
			}
		}
		return storage;
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider registries) {
		ListTag list = new ListTag();
		for (var poi : pois.entrySet()) {
			list.add(Util.make(new CompoundTag(), (tag) -> {
				tag.putString("block", poi.getValue().getRegisteredName());
				tag.putLong("pos", poi.getKey().asLong());
			}));
		}
		compoundTag.put("pois", list);
		return compoundTag;
	}
}
