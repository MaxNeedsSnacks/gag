package ky.someone.mods.gag.block.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import org.jetbrains.annotations.Nullable;

public record DirectionalProxyCapabilityProvider<T>(
		BlockCapability<T, Direction> cap
) implements IBlockCapabilityProvider<T, Direction> {
	@Override
	public @Nullable T getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction context) {
		var facing = state.getValue(DirectionalBlock.FACING);
		return level.getCapability(cap, pos.relative(facing), facing.getOpposite());
	}
}
