package net.flansflame.flans_star_forge.items.item;

import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.flansflame.flans_star_forge.pipe.PipeConnection;
import net.flansflame.flans_star_forge.pipe.energy.EnergyCableNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WrenchItem extends Item {
    public WrenchItem(Properties build) {
        super(build);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof EnergyCableBlock)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) return InteractionResult.SUCCESS;

        PipeConnection current = EnergyCableBlock.getConnection(state, side);
        PipeConnection next = current.next();

        BlockState newState = state.setValue(
                EnergyCableBlock.getPropertyFor(side), next
        );

        level.setBlock(pos, newState, 3);

        EnergyCableNetworkManager.markDirtyAt(level, pos);

        return InteractionResult.CONSUME;
    }
}
