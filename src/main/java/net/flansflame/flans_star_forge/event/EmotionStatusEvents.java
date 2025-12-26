package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.Utils;
import net.flansflame.flans_star_forge.emotion.EmotionBehaviorRates;
import net.flansflame.flans_star_forge.emotion.EmotionStats;
import net.flansflame.flans_star_forge.emotion.Emotions;
import net.flansflame.flans_star_forge.mixin_accesor.IPlayerMixinAccessor;
import net.flansflame.flans_star_forge.world.damagesource.ModDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EmotionStatusEvents {

    @SubscribeEvent
    public static void onKillVillager(LivingDeathEvent event) {
        Entity sourceEntity = event.getSource().getEntity();
        if (event.getEntity() instanceof Villager && sourceEntity instanceof IPlayerMixinAccessor iPlayer) {
            iPlayer.addSanity(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.OUTLAW));
        }
    }

    @SubscribeEvent
    public static void onBreakBlockWithFist(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().getMainHandItem().is(Tags.Items.TOOLS) && event.getPlayer() instanceof IPlayerMixinAccessor iPlayer) {
            iPlayer.addFatigue(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.NOT_COOl));
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof IPlayerMixinAccessor iPlayer) {
            iPlayer.addSanity(-5);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof IPlayerMixinAccessor iPlayer && event.getDistance() >= 6) {
            iPlayer.addFatigue(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.NOT_COOl));
            iPlayer.addMotivation(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.TOUGH));
        }
    }

    private static final int inDimension$DAMAGE_TICK = 30;
    private static int inDimension$tick = 0;

    @SubscribeEvent
    public static void inDimension(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof IPlayerMixinAccessor iPlayer) {
            if (inDimension$tick >= inDimension$DAMAGE_TICK) {
                ResourceKey<Level> dimension = event.player.level().dimension();
                if (dimension.equals(Level.NETHER)) {
                    iPlayer.addFatigue(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.TOUGH));
                } else if (dimension.equals(Level.END)) {
                    iPlayer.addMotivation(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.TOUGH));
                } else if (!dimension.equals(Level.OVERWORLD)) {
                    iPlayer.addSanity(EmotionStats.behaviorRates2Amount(EmotionBehaviorRates.TOUGH));
                }
                inDimension$tick = 0;
            } else {
                inDimension$tick++;
            }
        }
    }

    private static final int damageOnInsanity$DAMAGE_TICK = 30;
    private static int damageOnInsanity$tick = 0;

    @SubscribeEvent
    public static void damageOnInsanity(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player instanceof IPlayerMixinAccessor iPlayer && Emotions.status2Emotion(iPlayer.flansStarForge$getStats()).is(Emotions.INSANE)) {
            if (player.level() instanceof ServerLevel server) {
                if (damageOnInsanity$tick >= damageOnInsanity$DAMAGE_TICK) {
                    player.hurt(Utils.createDamageSource(server, ModDamageTypes.WITHERING_WITH_ALL_WITHOUT_COOLDOWN_BYPASS), (player.getMaxHealth()) / 10f);
                    damageOnInsanity$tick = 0;
                } else {
                    damageOnInsanity$tick++;
                }
            }
        }
    }
}
