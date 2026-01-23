package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.effects.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MobStrengthenEvents {

    public static float DEFAULT_ATTACK_MULTIPLIER = 0.25f;
    public static float DEFAULT_ATTACKED_MULTIPLIER = 1.75f;
    public static float BLESSED_ATTACK_MULTIPLIER = 1.25f;
    public static float BLESSED_ATTACKED_MULTIPLIER = 0.75f;

    @SubscribeEvent
    public static void onMobDamage(LivingHurtEvent event) {
        float amount = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();

        if (target == null) return;

        if (source.getEntity() instanceof LivingEntity entity) {

            if (entity instanceof Player && target instanceof Player) return;

            if (entity instanceof Player player){
                amount *= player.hasEffect(ModEffects.STARS_BLESSING.get()) ? BLESSED_ATTACK_MULTIPLIER : DEFAULT_ATTACK_MULTIPLIER;
            }

            if (target instanceof Player player){
                amount *= player.hasEffect(ModEffects.STARS_BLESSING.get()) ? BLESSED_ATTACKED_MULTIPLIER : DEFAULT_ATTACKED_MULTIPLIER;
            }
        }

        if (amount != event.getAmount()) {
            event.setAmount(amount);
        }
    }
}