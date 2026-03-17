package net.flansflame.flans_star_forge.screens.menu;

import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.blocks.entity.CombinerBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.FE2SdEConverterBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.HeatGeneratorBlockEntity;
import net.flansflame.flans_star_forge.screens.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class HeatGeneratorMenu extends AbstractMachineMenu {

    private static final int EXTRA_DATA_COUNT = 1;

    public HeatGeneratorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(EXTRA_DATA_COUNT));
    }

    public HeatGeneratorMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.HEAT_GENERATOR.get(), containerId, inventory, blockEntity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, HeatGeneratorBlockEntity.COAL_SLOT, 89, 35));
            this.addSlot(new SlotItemHandler(iItemHandler, HeatGeneratorBlockEntity.CLOCK_SLOT, 44, 15));
            this.addSlot(new SlotItemHandler(iItemHandler, HeatGeneratorBlockEntity.SDE_ENERGY_SLOT, 152, 6));
            this.addSlot(new SlotItemHandler(iItemHandler, HeatGeneratorBlockEntity.UPGRADE_SLOT, 8, 6));
        });

        this.init(data);
    }

    @Override
    protected int getSlotCount() {
        return 4;
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.HEAT_GENERATOR.get();
    }

    public boolean hasFuel(){
        return this.data.get(0) > 0;
    }
}
