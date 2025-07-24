package ky.someone.mods.gag.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Abstract class for all items registered by this mod.
 * Contains convenience methods.
 */
public abstract class GAGItem extends Item implements ItemWithSubsets {
	public GAGItem(Properties properties) {
		super(properties);
	}

	public boolean shouldDamage(@Nullable Player player, ItemStack stack) {
		return player == null || !player.isCreative();
	}

	public List<Component> getHoldingTooltip(Player player, ItemStack stack) {
		return List.of();
	}

	public List<Component> getUsingTooltip(Player player, ItemStack stack, int useTicks) {
		return List.of();
	}
}
