package net.flansflame.flans_star_forge.mixins;

import net.flansflame.flans_star_forge.world.entity.IOnRemoved;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.flansflame.flans_star_forge.world.entity.custom.WitherBombEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({WitherBoss.class})
public class WitherBossMixin implements IOnRemoved {

    @Override
    public void onRemove() {
        WitherBoss self = (WitherBoss) (Object) this;
        if (self.level() instanceof ServerLevel server){
            WitherBombEntity entityToSpawn = ModEntities.WITHER_BOMB.get().spawn(server, self.blockPosition(), MobSpawnType.COMMAND);
            if (entityToSpawn != null) {
                entityToSpawn.setPos(self.blockPosition().getCenter());
                entityToSpawn.setPos(new Vec3(entityToSpawn.getX(), entityToSpawn.getY() - 0.5, entityToSpawn.getZ()));
                entityToSpawn.setXRot(0);
                entityToSpawn.setYRot(0);
            }
        }
    }
}
