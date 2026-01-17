package net.flansflame.flans_star_forge.screens.render;

import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.screens.helper.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/LICENSE
 *
 *  Modified Version by: FlansFlame
 */
public class EnergyDisplayTooltipArea {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int dualOffset;
    private final StarDustEnergyStorage energy;

    public EnergyDisplayTooltipArea(int x, int y, int width, int height, int dualOffset, StarDustEnergyStorage energy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dualOffset = dualOffset;
        this.energy = energy;
    }

    private List<Component> getTooltips() {
        return List.of(Component.literal(energy.exGetEnergyStored() + " /"), Component.literal(energy.exGetMaxEnergyStored() + " FE"));
    }

    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, this.x, this.y, width, height) || (this.dualOffset != 0 && MouseUtil.isMouseOver(mouseX, mouseY, this.x + this.dualOffset, this.y, width, height))) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, this.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    public void render(GuiGraphics guiGraphics) {
        int stored = (int) (energy.exGetEnergyStored().divideAndGetFloat(energy.exGetMaxEnergyStored()) * height);

        guiGraphics.fillGradient(x, y + (height - stored), x + width,
                y + height, 0xff2a36b1, 0xff4a148c);

        if (dualOffset != 0) {
            guiGraphics.fillGradient(x + dualOffset, y + (height - stored), x + dualOffset + width,
                    y + height, 0xff2a36b1, 0xff4a148c);
        }
    }
}
