package net.flansflame.flans_star_forge.world.ai;

import net.flansflame.flans_star_forge.world.ai.custom.ShockWaveStellarAttack;
import net.flansflame.flans_star_forge.world.ai.custom.StabStellarAttack;
import net.flansflame.flans_star_forge.world.ai.custom.SummonFollowerStellarAttack;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class StellarAttackPhases {

    public static final ArrayList<StellarAttackPhase> ATTACK_PHASES = new ArrayList<>();

    public static final StellarAttackPhase STAB = register(new StabStellarAttack("stab"));
    public static final StellarAttackPhase SHOCK_WAVE = register(new ShockWaveStellarAttack("shock_wave", SoundEvents.TRIDENT_RETURN));
    public static final StellarAttackPhase CAST = register(new SummonFollowerStellarAttack("casting", SoundEvents.ENCHANTMENT_TABLE_USE));

    public static StellarAttackPhase register(StellarAttackPhase attackPhase) {
        ATTACK_PHASES.add(attackPhase);
        return attackPhase;
    }
}