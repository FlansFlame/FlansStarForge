package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.machine.BaseMachineBlock;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionEntry;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionWrapper;
import net.flansflame.flans_star_forge.blocks.util.WrappedHandler;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.QuintLongValue;
import net.flansflame.flans_star_forge.items.item.EnergizedClockItem;
import net.flansflame.flans_star_forge.items.item.PressurizedClockItem;
import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.flansflame.flans_star_forge.screens.menu.HeatGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class HeatGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int COAL_SLOT = 0;
    public static final int CLOCK_SLOT = 1;
    public static final int SDE_ENERGY_SLOT = 2;
    public static final int UPGRADE_SLOT = 3;

    protected final ContainerData data;
    private int fuelTime = 0;

    private final FluidTank fluidTank = new FluidTank(1000000000) {
        @Override
        protected void onContentsChanged() {
            HeatGeneratorBlockEntity.this.setChanged();
            if (level instanceof ServerLevel server) {
                server.sendBlockUpdated(HeatGeneratorBlockEntity.this.getBlockPos(), HeatGeneratorBlockEntity.this.getBlockState(), HeatGeneratorBlockEntity.this.getBlockState(), 3);

            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(Fluids.LAVA);
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public HeatGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HEAT_GENERATOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> HeatGeneratorBlockEntity.this.fuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> HeatGeneratorBlockEntity.this.fuelTime = value;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        return super.getCapability(capability, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag = fluidTank.writeToNBT(tag);
        tag.putInt("fuelTime", this.fuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.fuelTime = tag.getInt("fuelTime");
        this.fluidTank.readFromNBT(tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyFluidHandler = LazyOptional.of(() -> this.fluidTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyFluidHandler.invalidate();
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                HeatGeneratorBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case COAL_SLOT -> stack.is(Items.COAL);
                    case CLOCK_SLOT -> stack.getItem() instanceof PressurizedClockItem;
                    case SDE_ENERGY_SLOT -> stack.getItem() instanceof EnergizedClockItem;
                    case UPGRADE_SLOT -> false;
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
    }

    @Override
    protected Map<Direction, LazyOptional<WrappedHandler>> getDirectionWrappedHandlerMap() {
        return new InventoryDirectionWrapper(this.itemHandler,
                new InventoryDirectionEntry(Direction.UP, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.DOWN, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.NORTH, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.SOUTH, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.EAST, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.WEST, SDE_ENERGY_SLOT, false)
        ).directionMap;
    }

    @Override
    public QuintLong getEnergyCapacity() {
        return QuintLongValue.MILLION.get();
    }

    @Override
    public QuintLong getEnergyMaxReceive() {
        return QuintLongValue.ZERO.get();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new HeatGeneratorMenu(containerId, inventory, this, this.data);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);

        boolean changed = false;

        if (this.fuelTime <= 0){
            if (this.itemHandler.getStackInSlot(COAL_SLOT).is(Items.COAL)) {
                this.itemHandler.getStackInSlot(COAL_SLOT).shrink(1);
                this.fuelTime += 320;
            } else if (this.getFluidTank().getFluid().getFluid().isSame(Fluids.LAVA) && !this.getFluidTank().isEmpty()){
                int drain = this.getFluidTank().drain(1000, IFluidHandler.FluidAction.EXECUTE).getAmount();
                this.fuelTime += drain;
            }
            changed = true;
        }

        if (this.isLit()) {
            this.fuelTime--;
            this.getEnergyStorage().receiveEnergyFromInside(new QuintLong(1), false);
            changed = true;
        }

        boolean lit = state.getValue(BaseMachineBlock.LIT);
        if (lit != this.isLit()) {
            state = state.setValue(BaseMachineBlock.LIT, this.isLit());
            level.setBlock(pos, state, 3);
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private boolean isLit() {
        return this.fuelTime > 0 && this.getEnergyStorage().exGetMaxEnergyStored().copy().remove(this.getEnergyStorage().getEnergyStored()).isGreaterThan(0);
    }

    @Override
    public int getEnergySlotGettingFromItem() {
        return -1;
    }

    @Override
    public int getEnergySlotSend2Item() {
        return SDE_ENERGY_SLOT;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }
}
