package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.blocks.entity.DestructorBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.HeatGeneratorBlockEntity;
import net.flansflame.flans_star_forge.screens.helper.MouseUtil;
import net.flansflame.flans_star_forge.screens.menu.HeatGeneratorMenu;
import net.flansflame.flans_star_forge.screens.render.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeatGeneratorScreen extends AbstractMachineScreen<HeatGeneratorMenu> {

    private static final int FLUID_TANK_X = 35;
    private static final int FLUID_TANK_Y = 35;
    private static final int FLUID_TANK_WIDTH = 34;
    private static final int FLUID_TANK_HEIGHT = 38;

    private FluidTankRenderer fluidRenderer;

    public HeatGeneratorScreen(HeatGeneratorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        FluidTank fluidTank = ((HeatGeneratorBlockEntity) this.menu.blockEntity).getFluidTank();

        this.fluidRenderer = new FluidTankRenderer(fluidTank.getCapacity(), true, FLUID_TANK_WIDTH, FLUID_TANK_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        FluidTank fluidTank = ((HeatGeneratorBlockEntity) this.menu.blockEntity).getFluidTank();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (isMouseAboveArea(mouseX, mouseY, x, y, FLUID_TANK_X, FLUID_TANK_Y, this.fluidRenderer)) {
            guiGraphics.renderTooltip(this.font, this.fluidRenderer.getTooltip(fluidTank.getFluid(), TooltipFlag.NORMAL), Optional.empty(), mouseX - x, mouseY - y);
        }

        this.fluidRenderer.render(guiGraphics, x + FLUID_TANK_X, y + FLUID_TANK_Y, fluidTank.getFluid());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.menu.hasFuel()) {
            guiGraphics.blit(this.texture, x + 126, y + 48, 176, 0, 10, 9);
        }
    }

    @Override
    protected String getTextureName() {
        return "heat_generator";
    }

    @Override
    protected int @Nullable [] getArrowPos() {
        return null;
    }

    @Override
    protected int @Nullable [] getArrowTexturePos() {
        return null;
    }

    @Override
    protected int @Nullable [] getArrowSize() {
        return null;
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
