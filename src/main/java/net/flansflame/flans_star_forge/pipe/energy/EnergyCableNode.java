package net.flansflame.flans_star_forge.pipe.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;

public record EnergyCableNode(
        BlockEntity blockEntity,
        Direction side,
        IEnergyStorage energy
) {}
