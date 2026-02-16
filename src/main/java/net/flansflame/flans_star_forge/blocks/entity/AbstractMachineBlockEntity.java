package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.machine.CombinerBlock;
import net.flansflame.flans_star_forge.blocks.util.WrappedHandler;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.QuintLongValue;
import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.items.item.EnergizedClockItem;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider {

    protected final ItemStackHandler itemHandler;

    private final StarDustEnergyStorage energyStorage = new StarDustEnergyStorage(this.getEnergyCapacity(), this.getEnergyMaxReceive(), this.getEnergyMaxExtract(), this.getDefaultEnergy()) {
        @Override
        public void onEnergyChanged() {
            AbstractMachineBlockEntity.this.setChanged();
            AbstractMachineBlockEntity.this.getLevel().sendBlockUpdated(AbstractMachineBlockEntity.this.getBlockPos(), AbstractMachineBlockEntity.this.getBlockState(), AbstractMachineBlockEntity.this.getBlockState(), 3);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<StarDustEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemHandler = this.getItemHandler();
    }

    public void drops() {
        if (this.level == null) return;

        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
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

            if (this.getDirectionWrappedHandlerMap().containsKey(side)) {
                Direction localDirection = this.getBlockState().getValue(CombinerBlock.FACING);

                if (side == Direction.DOWN || side == Direction.UP) {
                    return this.getDirectionWrappedHandlerMap().get(side).cast();
                }

                return switch (localDirection) {
                    default -> this.getDirectionWrappedHandlerMap().get(side.getOpposite()).cast();
                    case EAST -> this.getDirectionWrappedHandlerMap().get(side.getClockWise()).cast();
                    case SOUTH -> this.getDirectionWrappedHandlerMap().get(side).cast();
                    case WEST -> this.getDirectionWrappedHandlerMap().get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(capability, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyItemHandler = LazyOptional.of(() -> itemHandler);
        this.lazyEnergyHandler = LazyOptional.of(this::getEnergyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
        this.lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", this.itemHandler.serializeNBT());
        this.getEnergyStorage().serializeNBT(tag);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.itemHandler.deserializeNBT(tag.getCompound("inventory"));
        this.getEnergyStorage().deserializeNBT(tag);

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

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.getEnergySlotGettingFromItem() > -1){
            ItemStack itemStack = this.itemHandler.getStackInSlot(this.getEnergySlotGettingFromItem());

            if (itemStack.getItem() instanceof EnergizedClockItem && EnergizedClockItem.getStoredEnergy(itemStack).isGreaterThan(0)){
                QuintLong drained = EnergizedClockItem.drain(itemStack, this.getEnergyStorage().getSpace());

                this.getEnergyStorage().receiveEnergy(drained, false);
            }
        }

        if (this.getEnergySlotSend2Item() > -1){
            ItemStack itemStack = this.itemHandler.getStackInSlot(this.getEnergySlotSend2Item());

            if (itemStack.getItem() instanceof EnergizedClockItem && EnergizedClockItem.getSpace(itemStack).isGreaterThan(0)){
                QuintLong drained = this.getEnergyStorage().extractEnergy(EnergizedClockItem.getSpace(itemStack), false);

                EnergizedClockItem.send(itemStack, drained);
            }

        }
    }

    public abstract ItemStackHandler getItemHandler();

    protected abstract Map<Direction, LazyOptional<WrappedHandler>> getDirectionWrappedHandlerMap();

    public abstract QuintLong getEnergyCapacity();

    public abstract int getEnergySlotGettingFromItem();

    public QuintLong getEnergyMaxTransfer() {
        return this.getEnergyCapacity();
    }

    public QuintLong getEnergyMaxReceive() {
        return this.getEnergyMaxTransfer();
    }

    public QuintLong getEnergyMaxExtract() {
        return this.getEnergyMaxTransfer();
    }

    public QuintLong getDefaultEnergy() {
        return QuintLongValue.ZERO.get();
    }

    public StarDustEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public int getEnergySlotSend2Item() {
        return -1;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}
