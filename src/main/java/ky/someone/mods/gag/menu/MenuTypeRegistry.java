package ky.someone.mods.gag.menu;


import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface MenuTypeRegistry {
	DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, GAGUtil.MOD_ID);

	Supplier<MenuType<LabelingMenu>> LABELING = MENUS.register("labeling", () -> new MenuType<>(LabelingMenu::new, FeatureFlagSet.of()));
}
