package net.flansflame.flans_star_forge.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class StarDustEnergyStorage implements IEnergyStorage {

    public static final Capability<StarDustEnergyStorage> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    protected QuintLong energy;
    protected QuintLong capacity;
    protected QuintLong maxReceive;
    protected QuintLong maxExtract;

    public StarDustEnergyStorage(QuintLong capacity) {
        this(capacity, capacity, capacity, QuintLongValue.ZERO.get());
    }

    public StarDustEnergyStorage(QuintLong capacity, QuintLong maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, QuintLongValue.ZERO.get());
    }

    public StarDustEnergyStorage(QuintLong capacity, QuintLong maxReceive, QuintLong maxExtract) {
        this(capacity, maxReceive, maxExtract, QuintLongValue.ZERO.get());
    }

    public StarDustEnergyStorage(QuintLong capacity, QuintLong maxReceive, QuintLong maxExtract, QuintLong energy) {
        if (energy.isGreaterThan(capacity)) energy.set(capacity);
        if (maxReceive.isGreaterThan(capacity)) maxReceive.set(capacity);
        if (maxExtract.isGreaterThan(capacity)) maxExtract.set(capacity);

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) return 0;

        QuintLong realCapacity = capacity.copy().remove(energy);
        QuintLong received = new QuintLong(maxReceive);

        if (received.isGreaterThan(this.maxReceive)) received.set(this.maxReceive);
        if (received.isGreaterThan(realCapacity)) received.set(realCapacity);

        if (!simulate) {
            this.energy.add(received);
        }

        if (received.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }
        return received.toInteger();
    }

    public QuintLong receiveEnergy(QuintLong maxReceive, boolean simulate) {
        if (!this.canReceive()) return QuintLongValue.ZERO.get();

        QuintLong realCapacity = capacity.copy().remove(energy);

        if (maxReceive.isGreaterThan(this.maxReceive)) {
            maxReceive.set(this.maxReceive);
        }
        if (maxReceive.isGreaterThan(realCapacity)) {
            maxReceive.set(realCapacity);
        }

        if (!simulate) {
            this.energy.add(maxReceive);
        }

        if (maxReceive.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }
        return maxReceive;
    }

    public QuintLong receiveEnergyFromInside(QuintLong maxReceive, boolean simulate) {
        QuintLong realCapacity = capacity.copy().remove(energy);

        if (maxReceive.isGreaterThan(this.exGetMaxEnergyStored()))
            maxReceive.set(this.exGetMaxEnergyStored());
        if (maxReceive.isGreaterThan(realCapacity))
            maxReceive.set(realCapacity);

        if (!simulate) {
            this.energy.add(maxReceive);
        }

        if (maxReceive.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }
        return maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) return 0;

        QuintLong extract = new QuintLong(maxExtract);

        if (extract.isGreaterThan(this.maxExtract)) extract.set(this.maxExtract);
        if (extract.isGreaterThan(this.energy)) extract.set(this.energy);

        if (!simulate) {
            this.energy.remove(extract);
        }

        if (extract.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }

        return extract.toInteger();
    }

    public QuintLong extractEnergy(QuintLong maxExtract, boolean simulate) {
        if (!this.canExtract()) return QuintLongValue.ZERO.get();

        if (maxExtract.isGreaterThan(this.maxExtract)) maxExtract.set(this.maxExtract);
        if (maxExtract.isGreaterThan(this.energy)) maxExtract.set(this.energy);

        if (!simulate) {
            this.energy.remove(maxExtract);
        }

        if (maxExtract.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }

        return maxExtract;
    }

    public QuintLong extractEnergyFromInside(QuintLong maxExtract, boolean simulate) {
        if (maxExtract.isGreaterThan(this.exGetMaxEnergyStored())) maxExtract.set(this.exGetMaxEnergyStored());
        if (maxExtract.isGreaterThan(this.energy)) maxExtract.set(this.energy);

        if (!simulate) {
            this.energy.remove(maxExtract);
        }

        if (maxExtract.isGreaterThan(QuintLongValue.ZERO.get())) {
            this.onEnergyChanged();
        }

        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return this.energy.toInteger();
    }

    public QuintLong exGetEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity.toInteger();
    }

    public QuintLong exGetMaxEnergyStored() {
        return this.capacity;
    }

    public QuintLong getSpace() {
        return this.capacity.copy().remove(this.energy);
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract.isGreaterThan(QuintLongValue.ZERO.get());
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive.isGreaterThan(QuintLongValue.ZERO.get());
    }

    public void serializeNBT(CompoundTag tag) {
        long[] layers = this.energy.getLayer();

        for (int i = 0; i < layers.length; i++) {
            tag.putLong("energy_layer" + i, layers[i]);
        }
    }

    public void deserializeNBT(CompoundTag tag) {
        long[] layers = new long[QuintLong.LAYER_SIZE];

        for (int i = 0; i < layers.length; i++) {
            layers[i] = tag.getLong("energy_layer" + i);
        }
        this.energy.set(QuintLong.createFromList(layers));
    }

    public abstract void onEnergyChanged();
}
