package ky.someone.mods.gag.item;

import ky.someone.mods.gag.util.tooltip.TooltipSink;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Temporary class implementing certain methods of
 * data extensions common to both Forge and Fabric, as
 * well as some general convenience methods.
 */
public abstract class GAGItem extends Item implements ItemWithSubsets {
	public GAGItem(Properties properties) {
		super(properties);
	}

	public boolean shouldDamage(@Nullable Player player, ItemStack stack) {
		return player == null || !player.isCreative();
	}

	public boolean shouldBob(ItemStack oldStack, ItemStack newStack) {
		return !oldStack.equals(newStack);
	}

	// forge
	public final boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return shouldBob(oldStack, newStack);
	}

	// fabric
	public final boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return shouldBob(oldStack, newStack);
	}

	public void getHoldingTooltip(Player player, ItemStack stack, TooltipSink sink) {
	}

	public void getUsingTooltip(Player player, ItemStack stack, int useTicks, TooltipSink sink) {
	}
}
