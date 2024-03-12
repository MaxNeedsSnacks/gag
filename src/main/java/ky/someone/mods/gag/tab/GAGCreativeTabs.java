package ky.someone.mods.gag.tab;

import dev.architectury.registry.CreativeTabRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.item.ItemRegistry;
import ky.someone.mods.gag.item.ItemWithSubsets;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface GAGCreativeTabs {
	DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GAGUtil.MOD_ID);

	Supplier<CreativeModeTab> GAG_TAB = TABS.register("gag", () -> CreativeTabRegistry.create(builder ->
			builder.icon(() -> ItemRegistry.HEARTHSTONE.get().getDefaultInstance())
					.title(Component.literal("Gadgets against Grind"))
					.displayItems((params, output) -> {
						for (var item : ItemRegistry.ITEMS.getEntries()) {
							output.accept(item.get().getDefaultInstance());
							if (item.get() instanceof ItemWithSubsets subsets) {
								output.acceptAll(subsets.getAdditionalSubItems());
							}
						}
					})
	));
}
