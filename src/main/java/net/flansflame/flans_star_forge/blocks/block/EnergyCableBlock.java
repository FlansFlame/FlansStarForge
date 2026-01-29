package net.flansflame.flans_star_forge.blocks.block;

import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.items.ModItems;
import net.flansflame.flans_star_forge.pipe.PipeConnection;
import net.flansflame.flans_star_forge.pipe.energy.EnergyCableNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.atomic.AtomicReference;

public class EnergyCableBlock extends Block {

    public static final EnumProperty<PipeConnection> NORTH = EnumProperty.create("north", PipeConnection.class);
    public static final EnumProperty<PipeConnection> SOUTH = EnumProperty.create("south", PipeConnection.class);
    public static final EnumProperty<PipeConnection> EAST = EnumProperty.create("east", PipeConnection.class);
    public static final EnumProperty<PipeConnection> WEST = EnumProperty.create("west", PipeConnection.class);
    public static final EnumProperty<PipeConnection> UP = EnumProperty.create("up", PipeConnection.class);
    public static final EnumProperty<PipeConnection> DOWN = EnumProperty.create("down", PipeConnection.class);

    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    private boolean setOrReset = false;

    public EnergyCableBlock(Properties build) {
        super(build);
        this.registerDefaultState(stateDefinition.any()
                .setValue(NORTH, PipeConnection.NONE)
                .setValue(SOUTH, PipeConnection.NONE)
                .setValue(EAST, PipeConnection.NONE)
                .setValue(WEST, PipeConnection.NONE)
                .setValue(UP, PipeConnection.NONE)
                .setValue(DOWN, PipeConnection.NONE)
                .setValue(LOCKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, LOCKED);
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

    public static BlockState setConnection(BlockState state, Direction dir, PipeConnection connection) {
        return switch (dir) {
            case NORTH -> state.setValue(NORTH, connection);
            case SOUTH -> state.setValue(SOUTH, connection);
            case EAST -> state.setValue(EAST, connection);
            case WEST -> state.setValue(WEST, connection);
            case UP -> state.setValue(UP, connection);
            case DOWN -> state.setValue(DOWN, connection);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return makeShape();
    }

    public VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0.375, 0.375, 0.625, 0.625, 0.625), BooleanOp.OR);

        return shape;
    }


    /*ENERGY HANDLING*/

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level instanceof ServerLevel server) {
            setOrResetStates(server, state, pos);

            /*
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.relative(direction);
                BlockState neighborState = server.getBlockState(neighbor);

                setOrResetStates(server, neighborState, neighbor);
            }
             */

            EnergyCableNetworkManager.pipePlaced(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {

        if (level instanceof ServerLevel server) {
            setOrResetStates(server, state, pos);
        }

        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level instanceof ServerLevel) {
            EnergyCableNetworkManager.pipeRemoved(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public static void setOrResetStates(ServerLevel server, BlockState state, BlockPos pos) {

        if (!state.is(ModBlocks.ENERGY_CABLE.get()) || state.getValue(LOCKED)) return;

        AtomicReference<BlockState> newState = new AtomicReference<>(state);

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);

            if (server.getBlockState(neighbor).is(ModBlocks.ENERGY_CABLE.get())) {
                newState.set(setConnection(newState.get(), direction, PipeConnection.BOTH));
            } else {
                newState.set(setConnection(newState.get(), direction, PipeConnection.NONE));
            }

            BlockEntity blockEntity = server.getBlockEntity(neighbor);
            if (blockEntity == null) {
                continue;
            }

            blockEntity.getCapability(StarDustEnergyStorage.CAPABILITY).ifPresent(capability -> {
                boolean canExtract = capability.canExtract();
                boolean canReceive = capability.canReceive();
                if (canExtract && canReceive) {
                    newState.set(setConnection(newState.get(), direction, PipeConnection.BOTH));
                } else if (canExtract) {
                    newState.set(setConnection(newState.get(), direction, PipeConnection.INPUT));
                } else if (canReceive) {
                    newState.set(setConnection(newState.get(), direction, PipeConnection.OUTPUT));
                } else {
                    newState.set(setConnection(newState.get(), direction, PipeConnection.NONE));
                }
            });
        }

        if (newState.get() != state) {
            server.setBlock(pos, newState.get(), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level instanceof ServerLevel server) {

            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;

            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.is(ModItems.WRENCH.get())) {
                BlockState newState = state;

                Direction side = result.getDirection();

                PipeConnection current = getConnection(newState, side);
                PipeConnection next = current.next();

                if (server.getBlockState(pos.relative(side)).is(ModBlocks.ENERGY_CABLE.get()) && current == PipeConnection.BOTH)
                    return InteractionResult.PASS;

                newState = setConnection(newState, side, next);

                player.displayClientMessage(Component.literal("> " + next.name().toUpperCase() + " <"), true);

                EnergyCableNetworkManager.markDirtyAt(level, pos);

                if (!newState.getValue(LOCKED)) {
                    newState = newState.setValue(LOCKED, true);
                }

                server.setBlock(pos, newState, 3);

                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, level, pos, player, hand, result);
    }

    public void setOrReset() {
        this.setOrReset = true;
    }
}