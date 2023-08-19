package ky.someone.mods.gag.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.item.ItemRegistry;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface BlockRegistry {
	DeferredRegister<Block> BLOCKS = DeferredRegister.create(GAGUtil.MOD_ID, Registries.BLOCK);

	RegistrySupplier<NoSolicitorsSign> NO_SOLICITORS_SIGN = register("no_solicitors", NoSolicitorsSign::new);

	static <B extends Block> RegistrySupplier<B> register(String name, Supplier<B> supplier) {
		return Util.make(BLOCKS.register(name, supplier), (block) -> {
			ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		});
	}

	static <B extends Block> RegistrySupplier<B> register(String name, Supplier<B> supplier, Consumer<Item.Properties> properties) {
		return Util.make(BLOCKS.register(name, supplier), (block) -> {
			ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), Util.make(new Item.Properties(), properties)));
		});
	}
}
