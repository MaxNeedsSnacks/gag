package ky.someone.mods.gag.util;

import ky.someone.mods.gag.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

public interface GAGUtil {

	String MOD_ID = ModConstants.MOD_ID;

	static void appendInfoTooltip(List<Component> tooltip, List<Component> info) {
		var isShift = Screen.hasShiftDown();

		tooltip.add(Component.literal("ℹ")
				.append(Component.literal(" (Shift)").withStyle(isShift ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY))
				.withStyle(Tooltips.INFO));

		if (isShift) {
			tooltip.addAll(info);
		}
	}

	static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	static ResourceKey<Level> dimension(ResourceLocation id) {
		return ResourceKey.create(Registries.DIMENSION, id);
	}

	static <U> U TODO() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
