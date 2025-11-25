package net.flansflame.flans_knowledge_lib.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface IBossBar {
    String getTextureId();

    float getEntityHpPercentage(LivingEntity entity);

    default boolean useBarCover() {
        return false;
    }

    default boolean useBarFrame() {
        return true;
    }

    default int[] imageScale() {
        return new int[]{182, 18};
    }
}
