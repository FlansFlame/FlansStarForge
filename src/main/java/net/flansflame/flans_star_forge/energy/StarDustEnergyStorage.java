package net.flansflame.flans_star_forge.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class StarDustEnergyStorage implements IEnergyStorage {
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

        if (maxReceive.isGreaterThan(this.maxReceive)) maxReceive.set(this.maxReceive);
        if (maxReceive.isGreaterThan(realCapacity)) maxReceive.set(realCapacity);

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

    @Override
    public boolean canExtract() {
        return this.maxExtract.isGreaterThan(QuintLongValue.ZERO.get());
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive.isGreaterThan(QuintLongValue.ZERO.get());
    }

    public void serializeNBT(CompoundTag tag) {
        tag.putLong("energy_layer0", this.energy.getLayer(0));
        tag.putLong("energy_layer1", this.energy.getLayer(1));
        tag.putLong("energy_layer2", this.energy.getLayer(2));
        tag.putLong("energy_layer3", this.energy.getLayer(3));
        tag.putLong("energy_layer4", this.energy.getLayer(4));
    }

    public void deserializeNBT(CompoundTag tag) {
        long energy_layer0 = tag.getLong("energy_layer0");
        long energy_layer1 = tag.getLong("energy_layer1");
        long energy_layer2 = tag.getLong("energy_layer2");
        long energy_layer3 = tag.getLong("energy_layer3");
        long energy_layer4 = tag.getLong("energy_layer4");

        this.energy.set(new QuintLong(energy_layer0, energy_layer1, energy_layer2, energy_layer3, energy_layer4));
    }

    public abstract void onEnergyChanged();
}
