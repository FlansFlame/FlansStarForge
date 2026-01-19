package net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.passive;

import net.flansflame.flans_star_forge.Utils;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.FailedNovaPassiveSkill;
import net.flansflame.flans_star_forge.entities.IUnremovableByFSFEntityProjectile;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.List;

public class ExplodeProjectilePassiveSkill extends FailedNovaPassiveSkill {

    public static final float INSTANT_KILL_RADIUS = 1.5f;
    public static final int KILL_TICK = 3;

    @Override
    public void onPassiveSkill(FailedNovaEntity nova, Entity entity) {
        if (!(entity instanceof IUnremovableByFSFEntityProjectile) && !(entity instanceof ItemEntity) && !(entity instanceof LivingEntity) && !(entity instanceof HangingEntity) && !(entity instanceof AbstractMinecart) && !(entity instanceof Boat) && !(entity instanceof FallingBlockEntity)) {

            boolean isInRadius = false;
            List<Entity> entities = nova.level().getEntitiesOfClass(Entity.class, nova.getBoundingBox().inflate(INSTANT_KILL_RADIUS)).stream().toList();
            for (Entity rEntity : entities) {
                if (entity.is(rEntity)) {
                    isInRadius = true;
                }
            }

            if (entity.tickCount > KILL_TICK || isInRadius) {
                if (entity.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 0, 0f, 0f, 0f, 0f);
                    server.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE);
                }

                Utils.exRemove(entity);
            }
        }
    }
}