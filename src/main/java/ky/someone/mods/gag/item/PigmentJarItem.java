package ky.someone.mods.gag.item;

import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.data.Pigment;
import ky.someone.mods.gag.util.GAGUtil;
import ky.someone.mods.gag.util.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PigmentJarItem extends GAGItem {

	public static final int MAX_AMOUNT = 64;
	public static final int DYE_AMOUNT = 4;

	public PigmentJarItem() {
		super(new Properties().stacksTo(16));
	}

	public static Pigment getPigment(ItemStack stack) {
		return stack.getOrDefault(GAGRegistry.PIGMENT_DATA, Pigment.EMPTY);
	}

	private static boolean hasPigment(ItemStack stack) {
		return !getPigment(stack).isEmpty();
	}

	public static boolean isNonEmptyJar(ItemStack stack) {
		return stack.is(GAGRegistry.PIGMENT_JAR.get()) && hasPigment(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
		if (!hasPigment(stack)) {
			list.add(Tooltips.FLAVOUR.lang("item.gag.pigment_jar.contents.empty").withStyle(ChatFormatting.ITALIC));
			GAGUtil.appendInfoTooltip(list, List.of(
					Tooltips.MAIN.lang("item.gag.pigment_jar.info.empty.1"),
					Tooltips.EXTRA.lang("item.gag.pigment_jar.info.empty.2")
			));
		} else {
			var pigment = Objects.requireNonNull(getPigment(stack));
			list.add(Tooltips.FLAVOUR.lang("item.gag.pigment_jar.contents",
					Tooltips.asStyledValue(pigment.amount(), MAX_AMOUNT / 2.0, Integer.toString(pigment.amount())),
					Component.literal(pigment.hex()).withStyle(s -> s.withColor(pigment.rgb()))
			));
			GAGUtil.appendInfoTooltip(list, List.of(
					Tooltips.MAIN.lang("item.gag.pigment_jar.info.filled.1"),
					Tooltips.EXTRA.lang("item.gag.pigment_jar.info.filled.2")
			));
		}
	}

	@Override
	public Collection<ItemStack> getAdditionalSubItems() {
		return Util.make(ItemStackLinkedSet.createTypeAndComponentsSet(), set -> {
			for (DyeColor color : DyeColor.values()) {
				set.add(Pigment.forText(color).asJar());
			}
		});
	}

	@Override
	public Component getName(ItemStack stack) {
		var name = super.getName(stack);
		var pigment = getPigment(stack);

		if (pigment.isEmpty()) return name;
		return name.copy().withStyle(s -> s.withColor(pigment.rgb()));
	}
}
