package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active;

import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.FailedNovaActiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.flansflame.flans_star_forge.entities.entity.StellarEndStageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class TeleportActiveSkill extends FailedNovaActiveSkill {


    public TeleportActiveSkill(String animationId, SoundEvent attackSound, boolean activateEvenIfNotNear) {
        super(animationId, attackSound, activateEvenIfNotNear);
    }

    @Override
    public void onAttack(FailedNovaEntity nova, LivingEntity target, float amount) {
        double x = nova.getX();
        double y = nova.getY();
        double z = nova.getZ();

        if (target == null) {
            if (nova.level() instanceof ServerLevel server) {
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                        .inflate(FailedNovaEntity.PASSIVE_SKILL_RADIUS), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entity : _entfound) {
                    if (entity instanceof Player player) {
                        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.setGameMode(GameType.SPECTATOR)) continue;

                        nova.setPos(new Vec3(player.getX(), player.getY(), player.getZ()));
                        break;
                    }
                }
            }
        } else {
            nova.setPos(new Vec3(target.getX(), target.getY(), target.getZ()));
        }
    }
}
