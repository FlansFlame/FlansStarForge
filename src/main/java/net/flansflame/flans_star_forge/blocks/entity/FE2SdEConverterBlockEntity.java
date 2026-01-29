package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.machine.BaseMachineBlock;
import net.flansflame.flans_star_forge.blocks.machine.FE2SdEConverterBlock;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionEntry;
import net.flansflame.flans_star_forge.blocks.util.InventoryDirectionWrapper;
import net.flansflame.flans_star_forge.blocks.util.WrappedHandler;
import net.flansflame.flans_star_forge.energy.ForgeEnergyStorage;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.QuintLongValue;
import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FE2SdEConverterBlockEntity extends AbstractMachineBlockEntity {

    private final ForgeEnergyStorage forgeEnergyStorage = new ForgeEnergyStorage(QuintLongValue.BILLION.get().toInteger(), QuintLongValue.BILLION.get().toInteger(), 0) {
        @Override
        public void onEnergyChanged() {
            FE2SdEConverterBlockEntity.this.setChanged();
            FE2SdEConverterBlockEntity.this.getLevel().sendBlockUpdated(FE2SdEConverterBlockEntity.this.getBlockPos(), FE2SdEConverterBlockEntity.this.getBlockState(), FE2SdEConverterBlockEntity.this.getBlockState(), 3);
        }
    };

    private static final int CONVERT_RATE_SDE = 1;
    private static final int CONVERT_RATE_FE = 100000000;

    public static final int FE_ENERGY_SLOT = 0;
    public static final int SDE_ENERGY_SLOT = 1;

    private LazyOptional<ForgeEnergyStorage> lazyForgeEnergyHandler = LazyOptional.empty();

    public FE2SdEConverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FE_2_SDE_CONVERTER.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ENERGY) {
            return this.lazyForgeEnergyHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyForgeEnergyHandler = LazyOptional.of(this::getForgeEnergyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyForgeEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("forge_energy", this.forgeEnergyStorage.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.forgeEnergyStorage.setEnergy(tag.getInt("forge_energy"));
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                FE2SdEConverterBlockEntity.this.setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case 0, 1 -> true;
                    default -> super.isItemValid(slot, stack);
                };
            }
        };
    }

    @Override
    protected Map<Direction, LazyOptional<WrappedHandler>> getDirectionWrappedHandlerMap() {
        return new InventoryDirectionWrapper(this.itemHandler,
                new InventoryDirectionEntry(Direction.UP, FE_ENERGY_SLOT, true),
                new InventoryDirectionEntry(Direction.DOWN, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.NORTH, FE_ENERGY_SLOT, true),
                new InventoryDirectionEntry(Direction.SOUTH, FE_ENERGY_SLOT, true),
                new InventoryDirectionEntry(Direction.EAST, SDE_ENERGY_SLOT, false),
                new InventoryDirectionEntry(Direction.WEST, FE_ENERGY_SLOT, true)
        ).directionMap;
    }

    @Override
    public QuintLong getEnergyCapacity() {
        return QuintLongValue.DECILLION.get();
    }

    @Override
    public QuintLong getEnergyMaxReceive() {
        return QuintLongValue.ZERO.get();
    }

    public ForgeEnergyStorage getForgeEnergyStorage() {
        return forgeEnergyStorage;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FE2SdEConverterMenu(containerId, inventory, this);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean changed = false;

        if (this.isLit()) {
            this.getForgeEnergyStorage().extractEnergyFromInside(CONVERT_RATE_FE, false);
            this.getEnergyStorage().receiveEnergyFromInside(new QuintLong(CONVERT_RATE_SDE), false);
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
        return this.getForgeEnergyStorage().getEnergyStored() >= CONVERT_RATE_FE && this.getEnergyStorage().exGetMaxEnergyStored().copy().remove(this.getEnergyStorage().getEnergyStored()).isGreaterThan(0);
    }
}
