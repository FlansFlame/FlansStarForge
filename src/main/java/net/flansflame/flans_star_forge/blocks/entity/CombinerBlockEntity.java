package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.machine.CombinerBlock;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionEntry;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionWrapper;
import net.flansflame.flans_star_forge.blocks.util.WrappedHandler;
import net.flansflame.flans_star_forge.energy.QuintLongValue;
import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.recipes.recipe.CombinerRecipe;
import net.flansflame.flans_star_forge.screens.menu.CombinerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class CombinerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            CombinerBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true;
                case 2, 4 -> false;
                case 3 -> stack.is(Blocks.COAL_BLOCK.asItem());
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    public static final int INPUT_1_SLOT = 0;
    public static final int INPUT_2_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int ENERGY_SLOT = 3;
    public static final int UPGRADE_SLOT = 4;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.UP, INPUT_1_SLOT, true),
                    new InventoryDirectionEntry(Direction.DOWN, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.NORTH, INPUT_2_SLOT, true),
                    new InventoryDirectionEntry(Direction.SOUTH, INPUT_1_SLOT, true),
                    new InventoryDirectionEntry(Direction.EAST, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.WEST, INPUT_2_SLOT, true)
            ).directionMap;
    private LazyOptional<StarDustEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;

    private final StarDustEnergyStorage ENERGY_STORAGE = new StarDustEnergyStorage(QuintLongValue.OCTILLION.get()) {
        @Override
        public void onEnergyChanged() {
            CombinerBlockEntity.this.setChanged();
            CombinerBlockEntity.this.getLevel().sendBlockUpdated(CombinerBlockEntity.this.getBlockPos(), CombinerBlockEntity.this.getBlockState(), CombinerBlockEntity.this.getBlockState(), 3);
        }
    };

    public CombinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMBINER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CombinerBlockEntity.this.progress;
                    case 1 -> CombinerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CombinerBlockEntity.this.progress = value;
                    case 1 -> CombinerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public StarDustEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void drops() {
        if (this.level == null) return;

        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.flans_star_forge.combiner");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == StarDustEnergyStorage.CAPABILITY) {
            return lazyEnergyHandler.cast();
        }

        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }

            if (directionWrappedHandlerMap.containsKey(side)) {
                Direction localDirection = this.getBlockState().getValue(CombinerBlock.FACING);

                if (side == Direction.DOWN || side == Direction.UP) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDirection) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(capability, side);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CombinerMenu(containerId, inventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", this.itemHandler.serializeNBT());
        tag.putInt("progress", this.progress);

        this.ENERGY_STORAGE.serializeNBT(tag);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.itemHandler.deserializeNBT(tag.getCompound("inventory"));
        this.progress = tag.getInt("progress");

        this.ENERGY_STORAGE.deserializeNBT(tag);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean changed = false;
        boolean lit = this.isLit();

        if (this.hasEnergyItem() && this.ENERGY_STORAGE.exGetMaxEnergyStored().copy().remove(this.ENERGY_STORAGE.exGetEnergyStored()).isGreaterOrSameThan(QuintLongValue.SEPTILLION.get())) {
            this.ENERGY_STORAGE.receiveEnergy(QuintLongValue.SEPTILLION.get(), false);
            this.itemHandler.getStackInSlot(ENERGY_SLOT).shrink(1);
        }

        if (this.isLit()) {
            this.progress++;
            this.ENERGY_STORAGE.extractEnergy(20, false);

            if (this.progress >= this.maxProgress) {
                this.craftItem();
                progress = 0;
            }

            changed = true;
        } else {
            progress = 0;
        }

        if (lit != this.isLit()) {
            state = state.setValue(CombinerBlock.LIT, this.isLit());
            level.setBlock(pos, state, 3);
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private boolean hasEnergyItem() {
        ItemStack itemStack = this.itemHandler.getStackInSlot(ENERGY_SLOT);
        if (itemStack.isEmpty()) return false;

        return itemStack.is(Blocks.COAL_BLOCK.asItem());
    }

    private boolean isLit() {
        return this.isOutputSlotReceivable() && this.hasRecipe() && this.hasEnergy();
    }

    private boolean hasEnergy() {
        return this.ENERGY_STORAGE.exGetEnergyStored().isGreaterOrSameThan(20L * maxProgress);
    }

    private void craftItem() {
        Optional<CombinerRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return;
        ItemStack resultItem = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_1_SLOT, 1, false);
        this.itemHandler.extractItem(INPUT_2_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(resultItem.getItem(), this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + resultItem.getCount()));
    }

    private boolean hasRecipe() {
        Optional<CombinerRecipe> recipe = this.getCurrentRecipe();

        if (recipe.isEmpty()) return false;
        ItemStack resultItem = recipe.get().getResultItem(null);

        return this.canInsertAmountInToOutputSlot(resultItem.getCount()) && this.canInsertItemIntoOutputSlot(resultItem.getItem());
    }

    private Optional<CombinerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CombinerRecipe.Type.INSTANCE, inventory, level);
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}