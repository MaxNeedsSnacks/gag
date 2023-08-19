package ky.someone.mods.gag.menu;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public interface MenuTypeRegistry {
	DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.MENU);

	RegistrySupplier<MenuType<LabelingMenu>> LABELING = MENUS.register("labeling", () -> new MenuType<>(LabelingMenu::new, FeatureFlagSet.of()));
}
