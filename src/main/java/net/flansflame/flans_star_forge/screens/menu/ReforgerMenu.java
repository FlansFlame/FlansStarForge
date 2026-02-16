package net.flansflame.flans_star_forge.screens.menu;

import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.blocks.entity.ReforgerBlockEntity;
import net.flansflame.flans_star_forge.screens.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ReforgerMenu extends AbstractMachineMenu {

    private static final int EXTRA_DATA_COUNT = 2;

    public ReforgerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(EXTRA_DATA_COUNT));
    }

    public ReforgerMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.REFORGER.get(), containerId, inventory, blockEntity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, ReforgerBlockEntity.INPUT_SLOT, 80, 15));
            this.addSlot(new SlotItemHandler(iItemHandler, ReforgerBlockEntity.OUTPUT_SLOT, 80, 57));
            this.addSlot(new SlotItemHandler(iItemHandler, ReforgerBlockEntity.ENERGY_SLOT, 152, 6));
            this.addSlot(new SlotItemHandler(iItemHandler, ReforgerBlockEntity.UPGRADE_SLOT, 8, 6));
        });

        this.init(data);
    }

    @Override
    protected int getSlotCount() {
        return 4;
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.REFORGER.get();
    }
}
