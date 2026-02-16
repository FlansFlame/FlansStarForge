package net.flansflame.flans_star_forge.screens.render;

import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.energy.StarDustEnergyStorage;
import net.flansflame.flans_star_forge.screens.helper.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Optional;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/LICENSE
 *
 *  Modified Version by: Kaupenjoe
 *  Modified Version by: FlansFlame
 */
public class ForgeEnergyDisplayTooltipArea {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final IEnergyStorage energy;

    public ForgeEnergyDisplayTooltipArea(int x, int y, int width, int height, IEnergyStorage energy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.energy = energy;
    }

    private List<Component> getTooltips() {
        return List.of(Component.literal(QuintLong.addComma(String.valueOf(energy.getEnergyStored())) + " /"), Component.literal(QuintLong.addComma(String.valueOf(energy.getMaxEnergyStored())) + " FE"));
    }

    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseOver(mouseX, mouseY, this.x, this.y, width, height)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, this.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    public void render(GuiGraphics guiGraphics) {
        int stored = (int) ((energy.getEnergyStored() / (float) energy.getMaxEnergyStored()) * height);

        guiGraphics.fillGradient(x, y + (height - stored), x + width,
                y + height, 0xffb51500, 0xff600b00);
    }
}