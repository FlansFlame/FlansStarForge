package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.screens.menu.CombinerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class CombinerScreen extends AbstractMachineScreen<CombinerMenu> {

    public static final int ARROW_X = 71;
    public static final int ARROW_Y = 34;
    private static final int ARROW_TEXTURE_X = 176;
    private static final int ARROW_TEXTURE_Y = 0;
    public static final int ARROW_WIDTH = 35;
    public static final int ARROW_HEIGHT = 21;

    public CombinerScreen(CombinerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "combiner";
    }

    @Override
    protected int @Nullable [] getArrowPos() {
        return new int[]{ARROW_X, ARROW_Y};
    }

    @Override
    protected int @Nullable [] getArrowTexturePos() {
        return new int[]{ARROW_TEXTURE_X, ARROW_TEXTURE_Y};
    }

    @Override
    protected int @Nullable [] getArrowSize() {
        return new int[]{ARROW_WIDTH, ARROW_HEIGHT};
    }
}

