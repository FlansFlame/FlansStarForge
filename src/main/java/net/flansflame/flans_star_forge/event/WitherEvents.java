package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.world.effect.ModEffects;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber
public class WitherEvents {

    @SubscribeEvent
    public static void onDefeatWither(LivingDeathEvent event){
        if (event.getEntity() instanceof WitherBoss wither &&
                event.getSource().getEntity() instanceof Player player &&
                player.hasEffect(ModEffects.STARS_BLESSING.get())
        ){

        }
    }

    @SubscribeEvent
    public static void onNearWither(LivingEvent.LivingTickEvent event){
        if (event.getEntity() instanceof WitherBoss wither){
            if (wither.level() instanceof ServerLevel server) {
                final Vec3 _center = new Vec3(wither.getX(), wither.getY(), wither.getZ());
                List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                        .inflate(40 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entity : _entfound) {
                    if (entity == wither || entity instanceof StellarEntity || (entity instanceof Player player && player.hasEffect(ModEffects.STARS_BLESSING.get()))){
                        continue;
                    }
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 600, 255));
                }
            }
        }
    }
}