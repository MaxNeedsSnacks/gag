package ky.someone.mods.gag.tab;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.item.ItemRegistry;
import ky.someone.mods.gag.item.ItemWithSubsets;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public interface GAGCreativeTabs {
	DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.CREATIVE_MODE_TAB);

	RegistrySupplier<CreativeModeTab> GAG_TAB = TABS.register("gag", () -> CreativeTabRegistry.create(builder ->
			builder.icon(() -> ItemRegistry.HEARTHSTONE.get().getDefaultInstance())
					.title(Component.literal("Gadgets against Grind"))
					.displayItems((params, output) -> {
						for (var item : ItemRegistry.ITEMS) {
							output.accept(item.get().getDefaultInstance());
							if(item.get() instanceof ItemWithSubsets subsets) {
								output.acceptAll(subsets.getAdditionalSubItems());
							}
						}
					})
	));
}
