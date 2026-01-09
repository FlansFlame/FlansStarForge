package net.flansflame.flans_star_forge.world.ai.end_stellar.custom;

import net.flansflame.flans_star_forge.world.ai.end_stellar.EndStellarAttackPhase;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class TeleportEndStellarPhase extends EndStellarAttackPhase {
    public TeleportEndStellarPhase(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(StellarEndStageEntity stellar, LivingEntity target, float amount) {
        double x = stellar.getX();
        double y = stellar.getY();
        double z = stellar.getZ();

        if (target == null) {
            if (stellar.level() instanceof ServerLevel server) {
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                        .inflate(StellarEndStageEntity.PASSIVE_SKILL_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entity : _entfound) {
                    if (entity instanceof Player player) {
                        stellar.setPos(new Vec3(player.getX(), player.getY(), player.getZ()));
                        break;
                    }
                }
            }
        } else {
            stellar.setPos(new Vec3(target.getX(), target.getY(), target.getZ()));
        }
    }
}
