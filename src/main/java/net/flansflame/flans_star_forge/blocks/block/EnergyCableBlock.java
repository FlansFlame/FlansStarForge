package net.flansflame.flans_star_forge.blocks.block;

import net.flansflame.flans_star_forge.pipe.PipeConnection;
import net.flansflame.flans_star_forge.pipe.energy.EnergyCableNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class EnergyCableBlock extends Block {

    public static final EnumProperty<PipeConnection> NORTH = EnumProperty.create("north", PipeConnection.class);
    public static final EnumProperty<PipeConnection> SOUTH = EnumProperty.create("south", PipeConnection.class);
    public static final EnumProperty<PipeConnection> EAST = EnumProperty.create("east", PipeConnection.class);
    public static final EnumProperty<PipeConnection> WEST = EnumProperty.create("west", PipeConnection.class);
    public static final EnumProperty<PipeConnection> UP = EnumProperty.create("up", PipeConnection.class);
    public static final EnumProperty<PipeConnection> DOWN = EnumProperty.create("down", PipeConnection.class);

    public EnergyCableBlock(Properties build) {
        super(build);
        this.registerDefaultState(stateDefinition.any()
                .setValue(NORTH, PipeConnection.BOTH)
                .setValue(SOUTH, PipeConnection.BOTH)
                .setValue(EAST, PipeConnection.BOTH)
                .setValue(WEST, PipeConnection.BOTH)
                .setValue(UP, PipeConnection.BOTH)
                .setValue(DOWN, PipeConnection.BOTH));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            EnergyCableNetworkManager.pipePlaced(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            EnergyCableNetworkManager.pipeRemoved(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public static PipeConnection getConnection(BlockState state, Direction dir) {
        return switch (dir) {
            case NORTH -> state.getValue(NORTH);
            case SOUTH -> state.getValue(SOUTH);
            case EAST -> state.getValue(EAST);
            case WEST -> state.getValue(WEST);
            case UP -> state.getValue(UP);
            case DOWN -> state.getValue(DOWN);
        };
    }

    public static EnumProperty<PipeConnection> getPropertyFor(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }
}