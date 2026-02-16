package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.screens.menu.ReforgerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class ReforgerScreen extends AbstractMachineScreen<ReforgerMenu> {

    public static final int ARROW_X = 85;
    public static final int ARROW_Y = 34;
    private static final int ARROW_TEXTURE_X = 176;
    private static final int ARROW_TEXTURE_Y = 0;
    public static final int ARROW_WIDTH = 7;
    public static final int ARROW_HEIGHT = 21;

    public ReforgerScreen(ReforgerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected String getTextureName() {
        return "reforger";
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
