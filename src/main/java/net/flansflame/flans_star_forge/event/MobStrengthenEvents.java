package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.world.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MobStrengthenEvents {

    @SubscribeEvent
    public static void onMobDamage(LivingHurtEvent event) {
        float amount = event.getAmount();

        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModEffects.STARS_BLESSING.get())) {
                event.setAmount(amount * 0.75f);
            } else {
                MobEffectInstance instance = player.getEffect(MobEffects.WITHER);
                if (instance != null && instance.getAmplifier() == 255) {
                    event.setAmount(amount * 4);
                } else {
                    event.setAmount(amount * 1.75f);
                }
            }
        } else {
            MobEffectInstance instance = event.getEntity().getEffect(MobEffects.WITHER);
            if (instance == null || instance.getAmplifier() != 255) {
                Entity sourceEntity = event.getSource().getEntity();
                if (sourceEntity instanceof Player sourcePlayer && sourcePlayer.hasEffect(ModEffects.STARS_BLESSING.get())) {
                    event.setAmount(amount * 1.125f);
                } else {
                    event.setAmount(amount * 0.125f);
                }
            }
        }
    }
}