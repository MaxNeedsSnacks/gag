package ky.someone.mods.gag.item;

import dev.shadowsoffire.placebo.color.GradientColor;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ky.someone.mods.gag.GAGUtil.TOOLTIP_MAIN;

public class TemporalPouchItem extends GAGItem {

	public static final TagKey<BlockEntityType<?>> DO_NOT_ACCELERATE = TagKey.create(Registries.BLOCK_ENTITY_TYPE, GAGUtil.id("do_not_accelerate"));

	public TemporalPouchItem() {
		super(new Item.Properties().stacksTo(1));
	}

	public static int getStoredGrains(ItemStack stack) {
		return stack.getOrDefault(GAGRegistry.GRAINS_OF_TIME_DATA, 0);
	}

	public static void setStoredGrains(ItemStack stack, int time) {
		int newStoredTime = Math.min(time, GAGConfig.temporalPouch.capacity());
		stack.set(GAGRegistry.GRAINS_OF_TIME_DATA, newStoredTime);
	}

	@Override
	public void verifyComponentsAfterLoad(ItemStack stack) {
		// TODO: maybe? reintroduce tiab migration??
	}

	public MutableComponent getTimeForDisplay(ItemStack stack) {
		int storedGrains = getStoredGrains(stack);
		int seconds = storedGrains * GAGConfig.temporalPouch.durationPerUse() / GAGConfig.temporalPouch.grainsUsed();
		int minutes = seconds / 60;
		int hours = seconds / 3600;

		String timeString = String.format("%ds", seconds);

		if (hours > 0) {
			timeString = String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
		} else {
			if (minutes > 0) {
				timeString = String.format("%dm %ds", minutes, seconds % 60);
			}
		}

		return Component.translatable("item.gag.time_sand_pouch.info.stored_grains", storedGrains, timeString);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, level, entity, itemSlot, isSelected);
		if (level.isClientSide || !(entity instanceof Player player) || player.isFakePlayer()) {
			return;
		}

		if (level.getGameTime() % 20 == 0) {
			int storedGrains = getStoredGrains(stack);
			if (storedGrains + 20 < GAGConfig.temporalPouch.capacity()) {
				setStoredGrains(stack, storedGrains + 20);
			}
		}

		// remove time from any other TIAB items in the player's inventory
		// because this is relatively expensive, only do it every 10 seconds,
		// and only on bottles that have time stored in them
		if (level.getGameTime() % (20 * 10) == 0 && getStoredGrains(stack) != 0) {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack invStack = player.getInventory().getItem(i);
				if (invStack.getItem() == this) {
					if (invStack != stack) {
						int otherTimeData = getStoredGrains(invStack);
						int myTimeData = getStoredGrains(stack);

						if (myTimeData < otherTimeData) {
							setStoredGrains(stack, 0);
						} else {
							setStoredGrains(invStack, 0);
						}
					}
				}
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();

		if (level.isClientSide) {
			return InteractionResult.PASS;
		}

		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();
		Player player = ctx.getPlayer();

		// good lord this is a mouthful...
		var beRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK_ENTITY_TYPE);
		var validBlockEntity = Optional.ofNullable(level.getBlockEntity(pos))
				.map(BlockEntity::getType)
				.flatMap(beRegistry::getResourceKey)
				.flatMap(beRegistry::getHolder)
				.filter(type -> !type.is(DO_NOT_ACCELERATE))
				.isPresent();

		var randomTickingState = level.getBlockState(pos).isRandomlyTicking();

		if (!(GAGConfig.temporalPouch.isLevelAllowed(level) && (validBlockEntity || GAGConfig.temporalPouch.allowRandomTicks() && randomTickingState))) {
			return InteractionResult.FAIL;
		}

		var baseDuration = 20 * GAGConfig.temporalPouch.durationPerUse();

		var accelerator = level.getEntitiesOfClass(TimeAcceleratorEntity.class, new AABB(pos)).stream().findFirst().orElse(null);

		if (accelerator == null) {
			// First use needs to create a new accelerator
			if (shouldDamage(player, stack) && getStoredGrains(stack) < grainsRequired(1)) {
				return InteractionResult.SUCCESS;
			}

			accelerator = Objects.requireNonNull(GAGRegistry.TIME_ACCELERATOR.get().create(level));
			accelerator.setPos(Vec3.atCenterOf(pos));
			accelerator.setTicksRemaining(baseDuration);
			level.addFreshEntity(accelerator);
		}

		int clicks = accelerator.getTimesAccelerated();
		if (clicks++ >= GAGConfig.temporalPouch.maxRate() || shouldDamage(player, stack) && getStoredGrains(stack) < grainsRequired(clicks)) {
			return InteractionResult.SUCCESS;
		}

		accelerator.setTimesAccelerated(clicks);
		accelerator.setTicksRemaining((accelerator.getTicksRemaining() + baseDuration) / 2);

		if (shouldDamage(player, stack)) {
			setStoredGrains(stack, getStoredGrains(stack) - grainsRequired(clicks));
		}

		playNote(level, pos, clicks);

		return InteractionResult.SUCCESS;
	}

	public int grainsRequired(int level) {
		return (1 << Math.max(0, level - 1)) * GAGConfig.temporalPouch.grainsUsed();
	}

	private void playNote(Level level, BlockPos pos, int rate) {
		var pitches = new int[]{-6, -4, -2, -1, 1, 3, 5, 6};
		var pitch = (float) Math.pow(2.0D, (pitches[(rate - 1) % 8]) / 12.0D);
		var sound = rate > 8 ? SoundEvents.NOTE_BLOCK_FLUTE : SoundEvents.NOTE_BLOCK_CHIME;
		level.playSound(null, pos, sound.value(), SoundSource.PLAYERS, 3.0F, pitch);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal("If I could save time in a ")
				.append(Component.literal("bottle").withStyle(ChatFormatting.STRIKETHROUGH))
				.append(Component.literal(" bundle..."))
				.withStyle(GAGUtil.TOOLTIP_FLAVOUR)
				.withStyle(ChatFormatting.ITALIC));

		GAGUtil.appendInfoTooltip(tooltip, List.of(
				Component.translatable("item.gag.time_sand_pouch.info.1").withStyle(TOOLTIP_MAIN),
				Component.translatable("item.gag.time_sand_pouch.info.2").withStyle(TOOLTIP_MAIN)
		));

		//   "item.gag.time_sand_pouch.info.stored_grains": "Contains %1$s Grains of Time (worth %2$s)",
		tooltip.add(getTimeForDisplay(stack).withStyle(style -> style.withColor(GradientColor.RAINBOW)));
	}

	@Override
	public boolean shouldBob(ItemStack oldStack, ItemStack newStack) {
		return false;
	}
}
