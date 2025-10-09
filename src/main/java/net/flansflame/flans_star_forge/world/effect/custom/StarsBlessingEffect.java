package net.flansflame.flans_star_forge.world.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class StarsBlessingEffect extends MobEffect {

    public StarsBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, -10464594);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int level) {
        return true;
    }
}