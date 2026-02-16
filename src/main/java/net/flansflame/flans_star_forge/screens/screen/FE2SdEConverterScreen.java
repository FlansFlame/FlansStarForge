package net.flansflame.flans_star_forge.screens.screen;

import net.flansflame.flans_star_forge.blocks.entity.FE2SdEConverterBlockEntity;
import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.flansflame.flans_star_forge.screens.render.ForgeEnergyDisplayTooltipArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class FE2SdEConverterScreen extends AbstractMachineScreen<FE2SdEConverterMenu> {

    private ForgeEnergyDisplayTooltipArea forgeEnergyTooltipArea;

    private static final int FORGE_ENERGY_BAR_X = 11;
    private static final int FORGE_ENERGY_BAR_Y = 26;
    private static final int FORGE_ENERGY_BAR_WIDTH = 10;
    private static final int FORGE_ENERGY_BAR_HEIGHT = 54;

    public FE2SdEConverterScreen(FE2SdEConverterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.forgeEnergyTooltipArea = new ForgeEnergyDisplayTooltipArea(x + FORGE_ENERGY_BAR_X, y + FORGE_ENERGY_BAR_Y, FORGE_ENERGY_BAR_WIDTH, FORGE_ENERGY_BAR_HEIGHT, ((FE2SdEConverterBlockEntity) this.menu.blockEntity).getForgeEnergyStorage());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.forgeEnergyTooltipArea.renderTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        this.forgeEnergyTooltipArea.render(guiGraphics);
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
