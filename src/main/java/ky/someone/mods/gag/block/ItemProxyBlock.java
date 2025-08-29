package ky.someone.mods.gag.block;

import com.mojang.serialization.MapCodec;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

@EventBusSubscriber(modid = GAGUtil.MOD_ID)
public class ItemProxyBlock extends DirectionalProxyBlock<IItemHandler> {
	public static final MapCodec<ItemProxyBlock> CODEC = MapCodec.unit(ItemProxyBlock::new);

	public ItemProxyBlock() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected BlockCapability<IItemHandler, Direction> getProxiedCapability() {
		return Capabilities.ItemHandler.BLOCK;
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlock(Capabilities.ItemHandler.BLOCK, GAGRegistry.ITEM_PROXY.get(), GAGRegistry.ITEM_PROXY.get());
	}
}
