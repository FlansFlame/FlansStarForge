package net.flansflame.flans_star_forge.world.item;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlansStarForge.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.UNI_STONE.get()))
                    .title(Component.translatable("tab." + FlansStarForge.MOD_ID + ".star_forge"))
                    .displayItems((pParameters, pOutput) -> {

                        ArrayList<RegistryObject<Item>> registries = new ArrayList<>();

                        registries.addAll(ModItems.ITEMS.getEntries());

                        registries.addAll(ModBlocks.ITEMS.getEntries());

                        for (RegistryObject<Item> registry : registries) {
                            pOutput.accept(registry.get());
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
