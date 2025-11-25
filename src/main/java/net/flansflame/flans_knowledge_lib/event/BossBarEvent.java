package net.flansflame.flans_knowledge_lib.event;

import net.flansflame.flans_knowledge_lib.world.entity.IBossBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BossBarEvent {

    private static final int MAX_BOSS_BAR_COUNT = 3;
    private static final float TEXTURE_MULTIPLIER = 1.5f;
    private static final int TEXTURE_OFFSET = 7;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderBossBar(RenderGuiOverlayEvent event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        if (minecraft.level == null || minecraft.player == null) return;

        List<LivingEntity> entities = minecraft.level.getEntitiesOfClass(LivingEntity.class, minecraft.player.getBoundingBox().inflate(64)).stream().toList();

        int bossBarCount = 0;

        for (LivingEntity entity : entities) {
            if (entity instanceof IBossBar bossBar) {
                if (bossBarCount >= MAX_BOSS_BAR_COUNT) break;

                ResourceLocation frameTexture = new ResourceLocation("flans_knowledge_lib", "textures/gui/bar/" + bossBar.getTextureId() + "_frame.png");
                ResourceLocation barTexture = new ResourceLocation("flans_knowledge_lib", "textures/gui/bar/" + bossBar.getTextureId() + ".png");
                ResourceLocation coverTexture = new ResourceLocation("flans_knowledge_lib", "textures/gui/bar/" + bossBar.getTextureId() + "_cover.png");

                int imageWidth = (int) (bossBar.imageScale()[0] * TEXTURE_MULTIPLIER);
                int imageHeight = (int) (bossBar.imageScale()[1] * TEXTURE_MULTIPLIER);

                int x = (int) (minecraft.getWindow().getGuiScaledWidth() / 2f - (imageWidth) / 2f);
                int y = (int) (imageHeight * bossBarCount + imageHeight / 2f * (bossBarCount + 1));

                int textX = (int) (minecraft.getWindow().getGuiScaledWidth() / 2f - font.width(entity.getDisplayName()) / 2f);
                int textY = y - font.lineHeight;

                guiGraphics.drawString(font, entity.getDisplayName(), textX, textY, -14540254);

                if (bossBar.useBarFrame()) {
                    guiGraphics.blit(frameTexture, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                }

                guiGraphics.blit(barTexture, x, y, 0, 0, (int) ((imageWidth) * bossBar.getEntityHpPercentage(entity)), imageHeight, (int) ((imageWidth) * bossBar.getEntityHpPercentage(entity)), imageHeight);

                if (bossBar.useBarCover()) {
                    guiGraphics.blit(coverTexture, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                }

                bossBarCount++;
            }
        }
    }
}