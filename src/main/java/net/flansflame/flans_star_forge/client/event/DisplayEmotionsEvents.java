package net.flansflame.flans_star_forge.client.event;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.config.ClientConfig;
import net.flansflame.flans_star_forge.emotion.EmotionStats;
import net.flansflame.flans_star_forge.emotion.Emotions;
import net.flansflame.flans_star_forge.world.entity.IHasEmotion;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DisplayEmotionsEvents {

    private static final ResourceLocation EMOTION_BAR_TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/emotion_bar.png");
    private static final int[] EMOTION_BAR_TEXTURE_SIZE = {128, 32};
    private static final ResourceLocation PLAYER_ICON_TEXTURE = new ResourceLocation("minecraft", "textures/block/crafting_table_top.png");
    private static final ResourceLocation STELLAR_ICON_TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/stellar_icon.png");
    private static final ResourceLocation NULL_STELLAR_ICON_TEXTURE = new ResourceLocation("minecraft", "textures/block/bedrock.png");
    private static final int ICON_TEXTURE_SIZE = 16;
    private static final ResourceLocation SANITY_BAR_TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/sanity_bar.png");
    private static final ResourceLocation FATIGUE_BAR_TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/fatigue_bar.png");
    private static final ResourceLocation MOTIVATION_BAR_TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/motivation_bar.png");
    private static final ResourceLocation STATS_BAR_COVER_TEXTURES = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/stats_bar_cover.png");
    private static final int[] STATUS_BAR_SIZE = {1, 15};
    private static final int STELLAR_DETECT_RANGE = 128;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderEmotions(RenderGuiOverlayEvent event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Level level = player.level();

        if (minecraft.level == null || !minecraft.level.isClientSide) return;

        if (!ClientConfig.DISABLE_EMOTION_BAR.get()) {

            StellarEntity stellar = null;
            for (StellarEntity entity : level.getEntitiesOfClass(StellarEntity.class, player.getBoundingBox().inflate(STELLAR_DETECT_RANGE))) {
                Entity owner = entity.getOwner();
                if (owner != null && owner.is(player)) {
                    stellar = entity;
                }
            }

            int sanity = ((IHasEmotion) player).flansStarForge$getSanity();
            int fatigue = ((IHasEmotion) player).flansStarForge$getFatigue();
            int motivation = ((IHasEmotion) player).flansStarForge$getMotivation();

            int stellarSanity = stellar != null ? stellar.flansStarForge$getSanity() : 0;
            int stellarFatigue = stellar != null ? stellar.flansStarForge$getFatigue() : 0;
            int stellarMotivation = stellar != null ? stellar.flansStarForge$getMotivation() : 0;

            int x = 0;
            int y = (minecraft.getWindow().getGuiScaledHeight() / 5) * 3;

            int playerX = x + 8;
            int stellarX = x + 67;

            int playerStatusIconX = playerX + ICON_TEXTURE_SIZE + 7;
            int stellarStatusIconX = stellarX + ICON_TEXTURE_SIZE + 7;

            int iconY = y + 8;

            int playerStatusX = playerStatusIconX + ICON_TEXTURE_SIZE + 6;
            int stellarStatusX = stellarStatusIconX + ICON_TEXTURE_SIZE + 6;

            int statusY = iconY + 1;

            guiGraphics.blit(EMOTION_BAR_TEXTURE, x, y, 0, 0, EMOTION_BAR_TEXTURE_SIZE[0], EMOTION_BAR_TEXTURE_SIZE[1], EMOTION_BAR_TEXTURE_SIZE[0], EMOTION_BAR_TEXTURE_SIZE[1]);

            guiGraphics.blit(PLAYER_ICON_TEXTURE, playerX, iconY, 0, 0, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE);
            guiGraphics.blit(Emotions.getEmotions((IHasEmotion) player).texture, playerStatusIconX, iconY, 0, 0, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE);

            guiGraphics.blit(SANITY_BAR_TEXTURE, playerStatusX, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);
            guiGraphics.blit(FATIGUE_BAR_TEXTURE, playerStatusX + 1, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);
            guiGraphics.blit(MOTIVATION_BAR_TEXTURE, playerStatusX + 2, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);

            guiGraphics.blit(STATS_BAR_COVER_TEXTURES, playerStatusX, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(sanity)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(sanity)));
            guiGraphics.blit(STATS_BAR_COVER_TEXTURES, playerStatusX + 1, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(fatigue)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(fatigue)));
            guiGraphics.blit(STATS_BAR_COVER_TEXTURES, playerStatusX + 2, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(motivation)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(motivation)));

            if (stellar != null) {
                guiGraphics.blit(STELLAR_ICON_TEXTURE, stellarX, iconY, 0, 0, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE);
                guiGraphics.blit(Emotions.getEmotions(stellar).texture, stellarStatusIconX, iconY, 0, 0, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE);

                guiGraphics.blit(SANITY_BAR_TEXTURE, stellarStatusX, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);
                guiGraphics.blit(FATIGUE_BAR_TEXTURE, stellarStatusX + 1, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);
                guiGraphics.blit(MOTIVATION_BAR_TEXTURE, stellarStatusX + 2, statusY, 0, 0, STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1], STATUS_BAR_SIZE[0], STATUS_BAR_SIZE[1]);

                guiGraphics.blit(STATS_BAR_COVER_TEXTURES, stellarStatusX, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarSanity)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarSanity)));
                guiGraphics.blit(STATS_BAR_COVER_TEXTURES, stellarStatusX + 1, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarFatigue)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarFatigue)));
                guiGraphics.blit(STATS_BAR_COVER_TEXTURES, stellarStatusX + 2, statusY, 0, 0, STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarMotivation)), STATUS_BAR_SIZE[0], (int) (STATUS_BAR_SIZE[1] * stats2ReversePercentage(stellarMotivation)));
            } else {
                guiGraphics.blit(NULL_STELLAR_ICON_TEXTURE, stellarX, iconY, 0, 0, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE);
            }
        }
    }

    private static float stats2ReversePercentage(int status) {
        int modStatus = switch (status) {
            case 0 -> 8;
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            case 8 -> 0;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };

        return modStatus / (float) EmotionStats.MAX_STATS;
    }
}