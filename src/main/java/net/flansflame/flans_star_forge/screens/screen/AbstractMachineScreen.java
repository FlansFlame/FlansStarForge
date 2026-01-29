package net.flansflame.flans_star_forge.screens.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.screens.menu.AbstractMachineMenu;
import net.flansflame.flans_star_forge.screens.render.EnergyDisplayTooltipArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public abstract class AbstractMachineScreen<T extends AbstractMachineMenu> extends AbstractContainerScreen<T> {

    protected final ResourceLocation texture = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/" + this.getTextureName() + "_gui.png");

    public static final int ENERGY_BAR_X = 152;
    public static final int ENERGY_BAR_Y = 26;
    public static final int ENERGY_BAR_WIDTH = 6;
    public static final int ENERGY_BAR_HEIGHT = 54;
    public static final int ENERGY_BAR_DUAL_OFFSET = 10;

    protected EnergyDisplayTooltipArea energyTooltipArea;

    public AbstractMachineScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.energyTooltipArea = new EnergyDisplayTooltipArea(x + ENERGY_BAR_X, y + ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, ENERGY_BAR_DUAL_OFFSET, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.energyTooltipArea.renderTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.texture);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(this.texture, x, y, 0, 0, this.imageWidth, this.imageHeight);

        this.renderProgressArrow(guiGraphics, x, y);

        this.energyTooltipArea.render(guiGraphics);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (this.getArrowPos() == null || this.getArrowTexturePos() == null || this.getArrowSize() == null) return;

        if (this.menu.isCrafting()) {
            guiGraphics.blit(this.texture, x + this.getArrowPos()[0], y + this.getArrowPos()[1], this.getArrowTexturePos()[0], this.getArrowTexturePos()[1], this.getArrowSize()[0], this.menu.getScaledProgress(this.getArrowSize()[1]));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected abstract String getTextureName();

    @Nullable
    protected abstract int[] getArrowPos();

    @Nullable
    protected abstract int[] getArrowTexturePos();

    @Nullable
    protected abstract int[] getArrowSize();
}
