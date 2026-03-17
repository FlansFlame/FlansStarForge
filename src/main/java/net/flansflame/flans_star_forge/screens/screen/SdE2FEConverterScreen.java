package net.flansflame.flans_star_forge.screens.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.flansflame.flans_star_forge.blocks.entity.FE2SdEConverterBlockEntity;
import net.flansflame.flans_star_forge.blocks.entity.SdE2FEConverterBlockEntity;
import net.flansflame.flans_star_forge.screens.menu.FE2SdEConverterMenu;
import net.flansflame.flans_star_forge.screens.menu.SdE2FEConverterMenu;
import net.flansflame.flans_star_forge.screens.render.EnergyDisplayTooltipArea;
import net.flansflame.flans_star_forge.screens.render.ForgeEnergyDisplayTooltipArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class SdE2FEConverterScreen extends AbstractMachineScreen<SdE2FEConverterMenu> {

    private ForgeEnergyDisplayTooltipArea forgeEnergyTooltipArea;

    private static final int FORGE_ENERGY_BAR_X = 155;
    private static final int FORGE_ENERGY_BAR_Y = 6;
    private static final int FORGE_ENERGY_BAR_WIDTH = 10;
    private static final int FORGE_ENERGY_BAR_HEIGHT = 74;

    public static final int ENERGY_BAR_X = 8;
    public static final int ENERGY_BAR_Y = 26;

    public SdE2FEConverterScreen(SdE2FEConverterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.energyTooltipArea = new EnergyDisplayTooltipArea(x + ENERGY_BAR_X, y + ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, ENERGY_BAR_DUAL_OFFSET, menu.blockEntity.getEnergyStorage());
        this.forgeEnergyTooltipArea = new ForgeEnergyDisplayTooltipArea(x + FORGE_ENERGY_BAR_X, y + FORGE_ENERGY_BAR_Y, FORGE_ENERGY_BAR_WIDTH, FORGE_ENERGY_BAR_HEIGHT, ((SdE2FEConverterBlockEntity) this.menu.blockEntity).getForgeEnergyStorage());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.energyTooltipArea.renderTooltips(guiGraphics, mouseX, mouseY, x, y);
        this.forgeEnergyTooltipArea.renderTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.texture);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(this.texture, x, y, 0, 0, this.imageWidth, this.imageHeight);

        this.energyTooltipArea.render(guiGraphics);
        this.forgeEnergyTooltipArea.render(guiGraphics);
    }

    @Override
    protected String getTextureName() {
        return "sde2fe_converter";
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
