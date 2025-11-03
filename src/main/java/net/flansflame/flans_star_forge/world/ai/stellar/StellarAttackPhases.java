package net.flansflame.flans_star_forge.world.ai.stellar;

import net.flansflame.flans_star_forge.world.ai.stellar.custom.ExplodeStellarAttack;
import net.flansflame.flans_star_forge.world.ai.stellar.custom.ShockWaveStellarAttack;
import net.flansflame.flans_star_forge.world.ai.stellar.custom.StabStellarAttack;
import net.flansflame.flans_star_forge.world.ai.stellar.custom.SummonFollowerStellarAttack;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class StellarAttackPhases {

    public static final ArrayList<StellarAttackPhase> ATTACK_PHASES = new ArrayList<>();

    public static final StellarAttackPhase STAB = register(new StabStellarAttack("stab"));
    public static final StellarAttackPhase SHOCK_WAVE = register(new ShockWaveStellarAttack("shock_wave", SoundEvents.TRIDENT_RETURN));
    public static final StellarAttackPhase SUMMON_FOLLOWER = register(new SummonFollowerStellarAttack("casting", SoundEvents.ENCHANTMENT_TABLE_USE));
    public static final StellarAttackPhase STAB_WITH_SPEAR = register(new StellarAttackPhase("stab_with_spear"));
    public static final StellarAttackPhase EXPLODE = register(new ExplodeStellarAttack("guard", SoundEvents.GENERIC_EXPLODE));

    public static StellarAttackPhase register(StellarAttackPhase attackPhase) {
        ATTACK_PHASES.add(attackPhase);
        return attackPhase;
    }
}