package ky.someone.mods.gag.item;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public interface ItemWithSubsets {
	default Collection<ItemStack> getAdditionalSubItems() {
		return Set.of();
	}
}
