package ky.someone.mods.gag.item;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.config.GAGClientConfig;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.item.data.TeleportPos;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergizedHearthstoneItem extends HearthstoneItem {

	public EnergizedHearthstoneItem() {
		super(() -> GAGConfig.hearthstone.energizedDurability());
	}

	public boolean isBound(ItemStack stack) {
		return stack.has(GAGRegistry.TELEPORT_TARGET_DATA);
	}

	@Override
	public TeleportPos getTeleportPos(@Nullable Player player, ItemStack stack) {
		return stack.get(GAGRegistry.TELEPORT_TARGET_DATA);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(getTargetText(null, stack));
		GAGUtil.appendInfoTooltip(tooltip, List.of(
				getTranslation("info_adv").withStyle(GAGUtil.TOOLTIP_MAIN),
				getTranslation("info_adv_2").withStyle(GAGUtil.TOOLTIP_MAIN),
				getTranslation("info_adv_3").withStyle(GAGUtil.TOOLTIP_MAIN),
				Component.translatable("info.gag.supports_unbreaking").withStyle(GAGUtil.TOOLTIP_EXTRA)
		));
	}

	public Component getTargetText(@Nullable Player player, ItemStack stack) {
		var target = getTeleportPos(player, stack);

		if (target != null) {
			if (GAGClientConfig.hearthstoneHidePosition) {
				return getTranslation("target.bound", getTranslation("target.hidden").withStyle(GAGUtil.TOOLTIP_FLAVOUR))
						.withStyle(GAGUtil.COLOUR_INFO);
			}

			var pos = target.pos();
			var level = target.level();

			var text = Component.translatable(String.format("(%.1f %.1f %.1f)", pos.x, pos.y, pos.z)).withStyle(GAGUtil.COLOUR_TRUE);

			if (player == null || !level.equals(player.level().dimension())) {
				text.append(" @ ").append(Component.translatable(level.location().toString()).withStyle(ChatFormatting.GRAY));
			}

			return getTranslation("target.bound", text).withStyle(GAGUtil.COLOUR_INFO);
		}

		return getTranslation("target.unbound").withStyle(GAGUtil.COLOUR_FALSE);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		if (!isBound(stack)) {
			if (player.isShiftKeyDown()) {
				var pos = new TeleportPos(player.level().dimension(), player.position(), player.getYRot());
				stack.set(GAGRegistry.TELEPORT_TARGET_DATA, pos);

				player.playSound(GAGRegistry.HEARTHSTONE_THUNDER.get(), 0.5f, 1.25f);
				return InteractionResultHolder.success(stack);
			} else {
				return InteractionResultHolder.fail(stack);
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return isBound(stack) ? super.getUseDuration(stack, entity) : 0;
	}

	public static boolean lightningStrike(LightningBolt bolt, Entity entity) {
		if (entity instanceof ItemEntity itemEntity) {
			var stack = itemEntity.getItem();
			if (stack.is(GAGRegistry.HEARTHSTONE.get())) {
				var newStack = new ItemStack(GAGRegistry.ENERGIZED_HEARTHSTONE.get());
				// damage the new stack relative to the old one
				var damage = stack.getDamageValue() / (float) stack.getMaxDamage();
				newStack.setDamageValue((int) (newStack.getMaxDamage() * damage));
				// copy enchantments over to the new stack
				EnchantmentHelper.setEnchantments(newStack, EnchantmentHelper.getEnchantmentsForCrafting(stack));
				itemEntity.setItem(newStack);
				bolt.hitEntities.add(entity);
				return true;
			} else if (stack.is(GAGRegistry.ENERGIZED_HEARTHSTONE.get())) {
				// why are lightning bolts like this mojang...
				if (!bolt.hitEntities.contains(entity)) {
					// unbind the hearthstone first
					stack.remove(GAGRegistry.TELEPORT_TARGET_DATA);
					// and repair it by up to 25% of its durability on hit
					var damage = stack.getDamageValue() / (float) stack.getMaxDamage();
					stack.setDamageValue((int) (stack.getMaxDamage() * Math.max(0, damage - 0.25)));
				}
				itemEntity.setInvulnerable(true);
				bolt.hitEntities.add(entity);
				return true;
			}
		}
		return false;
	}
}
