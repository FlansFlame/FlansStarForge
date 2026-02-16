package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.blocks.entity.DestructorBlockEntity;
import net.flansflame.flans_star_forge.screens.helper.MouseUtil;
import net.flansflame.flans_star_forge.screens.menu.DestructorMenu;
import net.flansflame.flans_star_forge.screens.render.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DestructorScreen extends AbstractMachineScreen<DestructorMenu> {

    public static final int ARROW_X = 76;
    public static final int ARROW_Y = 34;
    private static final int ARROW_TEXTURE_X = 176;
    private static final int ARROW_TEXTURE_Y = 0;
    public static final int ARROW_WIDTH = 7;
    public static final int ARROW_HEIGHT = 21;

    private static final int FLUID_TANK_X = 107;
    private static final int FLUID_TANK_Y = 15;
    private static final int FLUID_TANK_WIDTH = 34;
    private static final int FLUID_TANK_HEIGHT = 38;

    private FluidTankRenderer fluidRenderer;

    public DestructorScreen(DestructorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        FluidTank fluidTank = ((DestructorBlockEntity) this.menu.blockEntity).getFluidTank();

        fluidRenderer = new FluidTankRenderer(fluidTank.getCapacity(), true, FLUID_TANK_WIDTH, FLUID_TANK_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        FluidTank fluidTank = ((DestructorBlockEntity) this.menu.blockEntity).getFluidTank();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (isMouseAboveArea(mouseX, mouseY, x, y, FLUID_TANK_X, FLUID_TANK_Y, this.fluidRenderer)) {
            guiGraphics.renderTooltip(this.font, this.fluidRenderer.getTooltip(fluidTank.getFluid(), TooltipFlag.NORMAL), Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        FluidTank fluidTank = ((DestructorBlockEntity) this.menu.blockEntity).getFluidTank();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.fluidRenderer.render(guiGraphics, x + FLUID_TANK_X, y + FLUID_TANK_Y, fluidTank.getFluid());
    }

    @Override
    protected String getTextureName() {
        return "destructor";
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

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
