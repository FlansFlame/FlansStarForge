package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class FE2SdEConverterScreen extends AbstractMachineScreen<FE2SdEConverterMenu> {
    public FE2SdEConverterScreen(FE2SdEConverterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "fe2sde_converter";
    }

    @Nullable
    @Override
    protected int[] getArrowPos() {
        return null;
    }

    @Nullable
    @Override
    protected int[] getArrowTexturePos() {
        return null;
    }

    @Nullable
    @Override
    protected int[] getArrowSize() {
        return null;
    }
}
