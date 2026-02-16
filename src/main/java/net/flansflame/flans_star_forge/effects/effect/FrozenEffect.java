package net.flansflame.flans_star_forge.effects.effect;

import net.flansflame.flans_star_forge.Utils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FrozenEffect extends MobEffect {
    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, Utils.getMcColor("00015C"));
    }



    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        entity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), new Vec3(0.1f, 0.1f, 0.1f));

        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
