package net.flansflame.flans_star_forge.pipe.energy;

import mekanism.common.content.network.EnergyNetwork;
import net.flansflame.flans_star_forge.blocks.block.EnergyCableBlock;
import net.flansflame.flans_star_forge.pipe.PipeConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class EnergyCableNetwork {

    public final Set<BlockPos> pipes = new HashSet<>();
    public final Set<EnergyCableNode> sources = new HashSet<>();
    public final Set<EnergyCableNode> sinks = new HashSet<>();

    private boolean dirty = true;
    private BlockPos rebuildStart;

    private static final int MAX_TRANSFER = 1000;

    public void markDirty(BlockPos start) {
        dirty = true;
        rebuildStart = start;
    }

    private void rebuildIfDirty(ServerLevel level) {
        if (!dirty || rebuildStart == null) return;

        pipes.clear();
        sources.clear();
        sinks.clear();

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
                be.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite())
                        .ifPresent(cap -> {
                            EnergyCableNode node = new EnergyCableNode(be, dir.getOpposite(), cap);
                            if (cap.canExtract()) sources.add(node);
                            if (cap.canReceive()) sinks.add(node);
                        });
                }
            }
        }

        dirty = false;
        rebuildStart = null;
    }

    public void tick(ServerLevel level) {
        rebuildIfDirty(level);

        if (sources.isEmpty() || sinks.isEmpty()) return;

        int perSink = MAX_TRANSFER / sinks.size();

        for (EnergyCableNode source : sources) {
            int available = source.energy().extractEnergy(MAX_TRANSFER, true);
            if (available <= 0) continue;

            int remaining = available;

            for (EnergyCableNode sink : sinks) {
                if (remaining <= 0) break;
                remaining -= sink.energy().receiveEnergy(perSink, false);
            }

            source.energy().extractEnergy(available - remaining, false);
        }
    }
}
