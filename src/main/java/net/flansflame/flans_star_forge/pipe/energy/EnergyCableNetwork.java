package net.flansflame.flans_star_forge.pipe.energy;

import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.pipe.PipeConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class EnergyCableNetwork {

    public final Set<BlockPos> pipes = new HashSet<>();
    public final Set<EnergyCableNode> producers = new HashSet<>();
    public final Set<EnergyCableNode> consumers = new HashSet<>();

    private boolean dirty = true;
    private BlockPos rebuildStart;

    private static final QuintLong MAX_TRANSFER = QuintLong.MAX_VALUE.copy();

    public void markDirty(BlockPos start) {
        dirty = true;
        rebuildStart = start;
    }

    private void rebuildIfDirty(ServerLevel level) {
        if (!dirty || rebuildStart == null) return;

        pipes.clear();
        producers.clear();
        consumers.clear();

        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(rebuildStart);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!pipes.add(pos)) continue;

            for (Direction dir : Direction.values()) {
                BlockPos next = pos.relative(dir);

                BlockState pipeState = level.getBlockState(pos);
                PipeConnection out = EnergyCableBlock.getConnection(pipeState, dir);

                if (!out.canConnect()) continue;

                BlockState nextState = level.getBlockState(next);

                if (nextState.getBlock() instanceof EnergyCableBlock) {
                    PipeConnection in = EnergyCableBlock.getConnection(
                            nextState, dir.getOpposite());

                    if (in.canConnect()) {
                        queue.add(next);
                    }
                }

                BlockEntity be = level.getBlockEntity(next);
                if (be == null) continue;

                if (out.canExtract() || out.canReceive()) {
                be.getCapability(StarDustEnergyStorage.CAPABILITY, dir.getOpposite())
                        .ifPresent(cap -> {
                            EnergyCableNode node = new EnergyCableNode(be, dir.getOpposite(), cap);
                            if (cap.canExtract()) producers.add(node);
                            if (cap.canReceive()) consumers.add(node);
                        });
                }
            }
        }

        dirty = false;
        rebuildStart = null;
    }

    public void tick(ServerLevel level) {
        rebuildIfDirty(level);

        if (producers.isEmpty() || consumers.isEmpty()) return;

        for (EnergyCableNode producer : producers) {
            QuintLong available = producer.energy().extractEnergy(MAX_TRANSFER.copy(), true);

            if (available.isSmallerOrSameThan(0)) continue;

            for (EnergyCableNode consumer : consumers) {
                QuintLong accepted = consumer.energy().receiveEnergy(available.copy(), false);
                producer.energy().extractEnergy(accepted.copy(), false);
                available.remove(accepted.copy());

                if (available.isSmallerOrSameThan(0)) break;
            }
        }
    }
}
