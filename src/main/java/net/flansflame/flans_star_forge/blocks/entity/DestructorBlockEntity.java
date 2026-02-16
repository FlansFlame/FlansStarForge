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
import net.flansflame.flans_star_forge.recipes.recipe.DestructorRecipe;
import net.flansflame.flans_star_forge.screens.menu.DestructorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class DestructorBlockEntity extends AbstractMachineBlockEntity {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_1_SLOT = 1;
    public static final int OUTPUT_2_SLOT = 2;
    public static final int FLUID_OUTPUT_SLOT = 3;
    public static final int ENERGY_SLOT = 4;
    public static final int UPGRADE_SLOT = 5;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;

    private final FluidTank fluidTank = new FluidTank(1000000000) {
        @Override
        protected void onContentsChanged() {
            DestructorBlockEntity.this.setChanged();
            if (level instanceof ServerLevel server) {
                server.sendBlockUpdated(DestructorBlockEntity.this.getBlockPos(), DestructorBlockEntity.this.getBlockState(), DestructorBlockEntity.this.getBlockState(), 3);

            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public DestructorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DESTRUCTOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DestructorBlockEntity.this.progress;
                    case 1 -> DestructorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DestructorBlockEntity.this.progress = value;
                    case 1 -> DestructorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return this.lazyFluidHandler.cast();
        }

        return super.getCapability(capability, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("progress", this.progress);
        tag = fluidTank.writeToNBT(tag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("progress");
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
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);

        ItemStack itemStack = this.itemHandler.getStackInSlot(FLUID_OUTPUT_SLOT);

        if (itemStack.getItem() instanceof PressurizedClockItem && PressurizedClockItem.getSpace(itemStack) > 0) {
            FluidStack drained = this.getFluidTank().drain(new FluidStack(PressurizedClockItem.getStoredFluidType(itemStack).isSame(Fluids.EMPTY) ? this.getFluidTank().getFluid().getFluid() : PressurizedClockItem.getStoredFluidType(itemStack), PressurizedClockItem.getSpace(itemStack)), IFluidHandler.FluidAction.EXECUTE);

            if (!drained.isEmpty()) {
                PressurizedClockItem.send(itemStack, drained.copy());
            }
        }

        boolean changed = false;

        if (this.isLit()) {
            this.progress++;

            if (this.progress >= this.maxProgress) {
                this.craftItem();
                this.getEnergyStorage().extractEnergyFromInside(new QuintLong(20L * maxProgress), false);
                progress = 0;
            }

            changed = true;
        } else {
            progress = 0;
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
        return this.isOutputSlotsReceivable() && this.hasRecipe() && this.hasEnergy2Craft();
    }

    private boolean hasEnergy2Craft() {
        return this.getEnergyStorage().exGetEnergyStored().isGreaterOrSameThan(20L * maxProgress);
    }

    private void craftItem() {
        Optional<DestructorRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return;
        ItemStack resultItem = recipe.get().getResultItem(null);
        ItemStack suResultItem = recipe.get().getSubResultItem(null);
        FluidStack fluidSResult = recipe.get().getFluidResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_1_SLOT, new ItemStack(resultItem.getItem(), this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).getCount() + resultItem.getCount()));
        this.itemHandler.setStackInSlot(OUTPUT_2_SLOT, new ItemStack(suResultItem.getItem(), this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).getCount() + suResultItem.getCount()));

        this.fluidTank.fill(fluidSResult, IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean hasRecipe() {
        Optional<DestructorRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return false;
        ItemStack resultItem = recipe.get().getResultItem(null);
        ItemStack suResultItem = recipe.get().getSubResultItem(null);
        FluidStack fluidSResult = recipe.get().getFluidResultItem(null);

        return this.canInsertItemIntoOutputSlot(resultItem.getItem()) && this.canInsertAmountInToOutputSlot(resultItem.getCount()) && this.canInsertItemIntoSubOutputSlot(suResultItem.getItem()) && this.canInsertAmountInToSubOutputSlot(suResultItem.getCount()) && this.canInsertFluid(fluidSResult);
    }

    private Optional<DestructorRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(DestructorRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).is(item);
    }

    private boolean canInsertAmountInToOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).getMaxStackSize() >= this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).getCount() + count;
    }

    private boolean isOutputSlotsReceivable() {
        return (this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_1_SLOT).getMaxStackSize()) && (this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).getMaxStackSize());
    }

    private boolean canInsertItemIntoSubOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).is(item);
    }

    private boolean canInsertAmountInToSubOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).getMaxStackSize() >= this.itemHandler.getStackInSlot(OUTPUT_2_SLOT).getCount() + count;
    }

    private boolean canInsertFluid(FluidStack fluid) {
        return this.fluidTank.getSpace() >= fluid.getAmount() && (this.fluidTank.getFluid().isEmpty() || this.fluidTank.getFluid().getFluid() == fluid.getFluid());
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(6) {
            @Override
            protected void onContentsChanged(int slot) {
                DestructorBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case INPUT_SLOT -> true;
                    case FLUID_OUTPUT_SLOT -> stack.getItem() instanceof PressurizedClockItem;
                    case OUTPUT_1_SLOT, OUTPUT_2_SLOT, UPGRADE_SLOT -> false;
                    case ENERGY_SLOT -> stack.getItem() instanceof EnergizedClockItem;
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
    }

    @Override
    protected Map<Direction, LazyOptional<WrappedHandler>> getDirectionWrappedHandlerMap() {
        return new InventoryDirectionWrapper(this.itemHandler,
                new InventoryDirectionEntry(Direction.UP, INPUT_SLOT, true),
                new InventoryDirectionEntry(Direction.DOWN, OUTPUT_1_SLOT, false),
                new InventoryDirectionEntry(Direction.NORTH, INPUT_SLOT, true),
                new InventoryDirectionEntry(Direction.SOUTH, INPUT_SLOT, true),
                new InventoryDirectionEntry(Direction.EAST, OUTPUT_2_SLOT, false),
                new InventoryDirectionEntry(Direction.WEST, INPUT_SLOT, true)
        ).directionMap;
    }

    @Override
    public QuintLong getEnergyCapacity() {
        return QuintLongValue.MILLION.get();
    }

    @Override
    public QuintLong getEnergyMaxExtract() {
        return new QuintLong();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new DestructorMenu(containerId, inventory, this, this.data);
    }

    @Override
    public int getEnergySlotGettingFromItem() {
        return ENERGY_SLOT;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }
}
