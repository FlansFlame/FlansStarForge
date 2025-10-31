package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.world.effect.ModEffects;
import net.flansflame.flans_star_forge.world.entity.custom.StarsClusterEntity;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MobStrengthenEvents {

    private static final float BLESSING_BARRIER_MULTIPLIER = 0.75f;
    private static final float NON_BLESSING_BARRIER_MULTIPLIER = 1.75f;
    private static final int NON_BLESSING_WITHER_MULTIPLIER = 4;

    private static final int MAX_EFFECT_AMPLIFIER = 255;

    public static final float BLESSING_ATTACK_MULTIPLIER = 1.125f;
    private static final float NON_BLESSING_ATTACK_MULTIPLIER = 0.125f;

    @SubscribeEvent
    public static void onMobDamage(LivingHurtEvent event) {
        float amount = event.getAmount();

        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModEffects.STARS_BLESSING.get())) {
                event.setAmount(amount * BLESSING_BARRIER_MULTIPLIER);
            } else {
                MobEffectInstance instance = player.getEffect(MobEffects.WITHER);
                if (instance != null && instance.getAmplifier() == MAX_EFFECT_AMPLIFIER) {
                    event.setAmount(amount * NON_BLESSING_BARRIER_MULTIPLIER);
                } else {
                    event.setAmount(amount * NON_BLESSING_WITHER_MULTIPLIER);
                }
            }
        } else {
            MobEffectInstance instance = event.getEntity().getEffect(MobEffects.WITHER);
            if ( instance == null || instance.getAmplifier() != MAX_EFFECT_AMPLIFIER) {
                Entity sourceEntity = event.getSource().getEntity();
                if ((sourceEntity instanceof Player sourcePlayer && sourcePlayer.hasEffect(ModEffects.STARS_BLESSING.get())) || sourceEntity instanceof StellarEntity || sourceEntity instanceof StarsClusterEntity) {
                    event.setAmount(amount * BLESSING_ATTACK_MULTIPLIER);
                } else {
                    event.setAmount(amount * NON_BLESSING_ATTACK_MULTIPLIER);
                }
            }
        }
    }
}