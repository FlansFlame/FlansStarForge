package net.flansflame.flans_star_forge.screens;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.screens.menu.CombinerMenu;
import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FlansStarForge.MOD_ID);

    public static final RegistryObject<MenuType<CombinerMenu>> COMBINER =
            registerMenuType(CombinerMenu::new, "combiner_menu");
    public static final RegistryObject<MenuType<FE2SdEConverterMenu>> FE_2_SDE_CONVERTER =
            registerMenuType(FE2SdEConverterMenu::new, "fe2sde_converter");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
