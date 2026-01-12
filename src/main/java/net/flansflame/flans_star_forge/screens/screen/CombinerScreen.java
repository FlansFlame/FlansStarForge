package net.flansflame.flans_star_forge.screens.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.screens.menu.CombinerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CombinerScreen extends AbstractContainerScreen<CombinerMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/combiner_gui.png");
    private static final int ARROW_X = 71;
    private static final int ARROW_Y = 34;
    private static final int ARROW_TEXTURE_X = 176;
    private static final int ARROW_TEXTURE_Y = 0;
    private static final int ARROW_WIDTH = 35;

    public CombinerScreen(CombinerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelX = 10000;
        this.inventoryLabelY = 10000;
        this.titleLabelX = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        this.renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (this.menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + ARROW_X, y + ARROW_Y, ARROW_TEXTURE_X, ARROW_TEXTURE_Y, ARROW_WIDTH, this.menu.getScaledProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
