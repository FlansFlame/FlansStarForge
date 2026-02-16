package net.flansflame.flans_star_forge.blocks.machine;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.entity.CombinerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CombinerBlock extends BaseMachineBlock {

    /*CONSTRUCTOR*/
    public CombinerBlock(Properties build) {
        super(build);
    }


    /*BLOCK ENTITY*/
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CombinerBlockEntity combiner) {
                combiner.drops();
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CombinerBlockEntity combiner) {
                NetworkHooks.openScreen((ServerPlayer) player, combiner, pos);
                return InteractionResult.SUCCESS;
            } else {
                throw new IllegalStateException("The CombinerBlockEntity did not open. **** :)");
            }
        }

        return super.use(state, level, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntities.COMBINER.get(),
                (sLevel, sPos, sState, sBlockEntity) -> sBlockEntity.tick(sLevel, sPos, sState));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CombinerBlockEntity(pos, state);
    }
}
