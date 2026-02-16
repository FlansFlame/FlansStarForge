package net.flansflame.flans_star_forge.screens.menu;

import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.blocks.entity.FE2SdEConverterBlockEntity;
import net.flansflame.flans_star_forge.screens.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FE2SdEConverterMenu extends AbstractMachineMenu {

    public FE2SdEConverterMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public FE2SdEConverterMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.FE_2_SDE_CONVERTER.get(), containerId, inventory, blockEntity, null);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, FE2SdEConverterBlockEntity.SDE_ENERGY_SLOT, 152, 6));
            this.addSlot(new SlotItemHandler(iItemHandler, FE2SdEConverterBlockEntity.UPGRADE_SLOT, 8, 6));
        });
    }

    @Override
    protected int getSlotCount() {
        return 2;
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.FE_2_SDE_CONVERTER.get();
    }
}
