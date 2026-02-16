package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.machine.BaseMachineBlock;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionEntry;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionWrapper;
import net.flansflame.flans_star_forge.blocks.util.WrappedHandler;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.QuintLongValue;
import net.flansflame.flans_star_forge.items.item.EnergizedClockItem;
import net.flansflame.flans_star_forge.recipes.recipe.CombinerRecipe;
import net.flansflame.flans_star_forge.recipes.recipe.ReforgerRecipe;
import net.flansflame.flans_star_forge.screens.menu.ReforgerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class ReforgerBlockEntity extends AbstractMachineBlockEntity {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int ENERGY_SLOT = 2;
    public static final int UPGRADE_SLOT = 3;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;

    public ReforgerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFORGER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ReforgerBlockEntity.this.progress;
                    case 1 -> ReforgerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ReforgerBlockEntity.this.progress = value;
                    case 1 -> ReforgerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);

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
        return this.isOutputSlotReceivable() && this.hasRecipe() && this.hasEnergy2Craft();
    }

    private boolean hasEnergy2Craft() {
        return this.getEnergyStorage().exGetEnergyStored().isGreaterOrSameThan(20L * maxProgress);
    }

    private void craftItem() {
        Optional<ReforgerRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return;
        ItemStack resultItem = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(resultItem.getItem(), this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + resultItem.getCount()));
    }

    private boolean hasRecipe() {
        Optional<ReforgerRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return false;
        ItemStack resultItem = recipe.get().getResultItem(null);

        return this.canInsertAmountInToOutputSlot(resultItem.getCount()) && this.canInsertItemIntoOutputSlot(resultItem.getItem());
    }

    private Optional<ReforgerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(ReforgerRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountInToOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
    }

    private boolean isOutputSlotReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                ReforgerBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case INPUT_SLOT -> true;
                    case OUTPUT_SLOT, UPGRADE_SLOT -> false;
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
                new InventoryDirectionEntry(Direction.DOWN, OUTPUT_SLOT, false),
                new InventoryDirectionEntry(Direction.NORTH, INPUT_SLOT, true),
                new InventoryDirectionEntry(Direction.SOUTH, INPUT_SLOT, true),
                new InventoryDirectionEntry(Direction.EAST, OUTPUT_SLOT, false),
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

    @Override
    public int getEnergySlotGettingFromItem() {
        return ENERGY_SLOT;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("progress", this.progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("progress");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ReforgerMenu(containerId, inventory, this, this.data);
    }
}
