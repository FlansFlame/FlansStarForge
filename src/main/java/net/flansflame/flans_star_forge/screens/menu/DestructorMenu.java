package net.flansflame.flans_star_forge.screens.menu;

import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.blocks.entity.CombinerBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.DestructorBlockEntity;
import net.flansflame.flans_star_forge.screens.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class DestructorMenu extends AbstractMachineMenu {

    private static final int EXTRA_DATA_COUNT = 2;

    public DestructorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(EXTRA_DATA_COUNT));
    }

    public DestructorMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.DESTRUCTOR.get(), containerId, inventory, blockEntity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.INPUT_SLOT, 71, 15));
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.OUTPUT_1_SLOT, 62, 57));
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.OUTPUT_2_SLOT, 80, 57));
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.FLUID_OUTPUT_SLOT, 116, 57));
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.ENERGY_SLOT, 152, 6));
            this.addSlot(new SlotItemHandler(iItemHandler, DestructorBlockEntity.UPGRADE_SLOT, 8, 6));
        });

        this.init(data);
    }

    @Override
    protected int getSlotCount() {
        return 6;
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.DESTRUCTOR.get();
    }
}
