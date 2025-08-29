package ky.someone.mods.gag.block;

import com.mojang.serialization.MapCodec;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@EventBusSubscriber(modid = GAGUtil.MOD_ID)
public class FluidProxyBlock extends DirectionalProxyBlock<IFluidHandler> {
	public static final MapCodec<FluidProxyBlock> CODEC = MapCodec.unit(FluidProxyBlock::new);

	public FluidProxyBlock() {
		super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected BlockCapability<IFluidHandler, Direction> getProxiedCapability() {
		return Capabilities.FluidHandler.BLOCK;
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlock(Capabilities.FluidHandler.BLOCK, GAGRegistry.FLUID_PROXY.get(), GAGRegistry.FLUID_PROXY.get());
	}
}
