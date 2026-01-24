package net.flansflame.flans_star_forge.pipe;

import net.minecraft.util.StringRepresentable;

public enum PipeConnection implements StringRepresentable {
    NONE,
    INPUT,
    OUTPUT,
    BOTH;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public PipeConnection next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public boolean canConnect() {
        return this != NONE;
    }

    public boolean canExtract() {
        return this == OUTPUT || this == BOTH;
    }

    public boolean canReceive() {
        return this == INPUT || this == BOTH;
    }
}