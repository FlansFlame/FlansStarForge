package net.flansflame.flans_star_forge.pipe.energy;

import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;

public record EnergyCableNode(
        BlockEntity blockEntity,
        Direction side,
        StarDustEnergyStorage energy
) {}
