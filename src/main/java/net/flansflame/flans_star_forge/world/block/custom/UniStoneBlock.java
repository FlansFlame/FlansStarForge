package net.flansflame.flans_star_forge.world.block.custom;

import net.flansflame.flans_star_forge.variable.ModVariables;
import net.flansflame.flans_star_forge.world.block.ModBlocks;
import net.flansflame.flans_star_forge.world.effect.ModEffects;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.flansflame.flans_star_forge.world.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class UniStoneBlock extends Block {
    public UniStoneBlock(Properties build) {
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

        if (!belowState.canOcclude()) {
            server.setBlock(belowPos, ModBlocks.UNI_STONE.get().defaultBlockState(), 3);
            server.removeBlock(pos, false);
            server.scheduleTick(pos, this, 1);
        } else {
            return;
        }
        belowPos = pos.below(2);
        belowState = server.getBlockState(belowPos);

        if (!belowState.canOcclude()) {
            server.setBlock(belowPos, ModBlocks.UNI_STONE.get().defaultBlockState(), 3);
            server.removeBlock(pos.below(), false);
            server.scheduleTick(belowPos, this, 1);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level instanceof ServerLevel server){
            StellarEntity entityToSpawn = ModEntities.STELLAR.get().spawn(server, pos, MobSpawnType.COMMAND);
            if (entityToSpawn != null) {
                entityToSpawn.setInvulnerable(true);
                entityToSpawn.tame(player);
                entityToSpawn.setYRot(server.getRandom().nextFloat() * 360F);
                entityToSpawn.setSitting(true);
            }
        }
        player.addItem(new ItemStack(ModItems.STARS_POWERSTONE.get()));
        ModVariables.starsBlessing(player, true);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
