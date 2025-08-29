package ky.someone.mods.gag.block.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public abstract class DirectionalProxyBlock<T> extends DirectionalBlock {

	public static final Property<Direction> FACINNG = DirectionalBlock.FACING;

	protected DirectionalProxyBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (!state.canSurvive(level, currentPos)) {
			level.scheduleTick(currentPos, this, 1);
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
		return switch (levelReader) {
			case Level level -> getCapability(level, pos, state) != null;
			default -> true;
		};
	}

	protected abstract BlockCapability<T, Direction> getProxiedCapability();

	protected @Nullable T getCapability(Level level, BlockPos pos, BlockState state) {
		var facing = state.getValue(FACING);
		return level.getCapability(getProxiedCapability(), pos.relative(facing), facing.getOpposite());
	}

	protected void registerCapabilities(RegisterCapabilitiesEvent event) {
		var cap = getProxiedCapability();
		event.registerBlock(cap, new DirectionalProxyCapabilityProvider<>(cap), this);
	}
}
