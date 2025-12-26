package net.flansflame.flans_star_forge.mixins;

import net.flansflame.flans_star_forge.config.CommonConfig;
import net.flansflame.flans_star_forge.world.block.ModBlocks;
import net.flansflame.flans_star_forge.world.tag.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WitherSkullBlock.class})
public class WitherSkullBlockMixin {

    @Shadow
    private static BlockPattern witherPatternFull;
    @Shadow
    private static BlockPattern witherPatternBase;

    @Inject(method = "getOrCreateWitherFull", at = @At("HEAD"), cancellable = true)
    private static void getOrCreateWitherFull(CallbackInfoReturnable<BlockPattern> cir) {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "#C#", "~#~")
                    .where('#', (block) -> block.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
                    .where('C', (block) -> flansStarForge$isCore(block.getState()))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL))))
                    .where('~', (block) -> block.getState().isAir()).build();
        }

        cir.setReturnValue(witherPatternFull);
    }

    @Inject(method = "getOrCreateWitherBase", at = @At("HEAD"), cancellable = true)
    private static void getOrCreateWitherBase(CallbackInfoReturnable<BlockPattern> cir) {
        if (witherPatternBase == null) {
            witherPatternBase = BlockPatternBuilder.start().aisle("   ", "#C#", "~#~")
                    .where('#', (block) -> block.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
                    .where('C', (block) -> flansStarForge$isCore(block.getState()))
                    .where('~', (block) -> block.getState().isAir()).build();
        }

        cir.setReturnValue(witherPatternBase);
    }

    @Unique
    private static boolean flansStarForge$isCore(BlockState block){
        return CommonConfig.LIGHT_MODE.get() ? block.is(ModTags.Blocks.ACTIVE_SOUL_CORE) : block.is(ModBlocks.ACTIVATED_EXTREME_SOUL_CORE.get()) || block.is(ModBlocks.ULTIMATE_SOUL_CORE.get());
    }
}