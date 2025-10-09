package net.flansflame.flans_star_forge.world.block.custom;

import net.flansflame.flans_star_forge.world.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MeteorBlock extends Block {

    public MeteorBlock(Properties build) {
        super(build);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state1, boolean b) {
        super.onPlace(state, level, pos, state1, b);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel server, BlockPos pos, RandomSource randomSource) {
        BlockPos belowPos = pos.below();
        BlockState belowState = server.getBlockState(belowPos);

        server.sendParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY() - 1, pos.getZ(), 8, 0.1, 0.1, 0.1, 0);
        server.sendParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 8, 0.1, 0.1, 0.1, 0);
        server.sendParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY() + 1, pos.getZ(), 8, 0.1, 0.1, 0.1, 0);

        if (!belowState.canOcclude()) {
            server.setBlock(belowPos, ModBlocks.METEOR.get().defaultBlockState(), 3);
            server.removeBlock(pos, false);
        } else {
            stop(server, pos);
            return;
        }
        belowPos = pos.below(2);
        belowState = server.getBlockState(belowPos);

        if (!belowState.canOcclude()) {
            server.setBlock(belowPos, ModBlocks.METEOR.get().defaultBlockState(), 3);
            server.removeBlock(pos.below(), false);
            server.scheduleTick(belowPos, this, 1);
        } else {
            stop(server, pos.below());
        }
    }

    public static void stop(ServerLevel server, BlockPos pos) {
        server.explode(null, pos.getX(), pos.getY() - 1, pos.getZ(), 8, Level.ExplosionInteraction.BLOCK);
        server.setBlock(pos, ModBlocks.UNI_STONE.get().defaultBlockState(), 3);
    }
}
