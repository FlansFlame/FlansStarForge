package net.flansflame.flans_star_forge.world.ai.end_stellar;

import net.flansflame.flans_star_forge.world.ai.end_stellar.custom.ExplodeEndStellarAttack;
import net.flansflame.flans_star_forge.world.ai.end_stellar.custom.ShockWaveEndStellarAttack;
import net.flansflame.flans_star_forge.world.ai.end_stellar.custom.StabEndStellarAttack;
import net.flansflame.flans_star_forge.world.ai.end_stellar.custom.SummonFollowerEndStellarAttack;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class EndStellarAttackPhases {

    public static final ArrayList<EndStellarAttackPhase> ATTACK_PHASES = new ArrayList<>();

    public static final EndStellarAttackPhase STAB = register(new StabEndStellarAttack("stab"));
    public static final EndStellarAttackPhase SHOCK_WAVE = register(new ShockWaveEndStellarAttack("shock_wave", SoundEvents.TRIDENT_RETURN));
    public static final EndStellarAttackPhase SUMMON_FOLLOWER = register(new SummonFollowerEndStellarAttack("casting", SoundEvents.ENCHANTMENT_TABLE_USE));
    public static final EndStellarAttackPhase STAB_WITH_SPEAR = register(new EndStellarAttackPhase("stab_with_spear"));
    public static final EndStellarAttackPhase EXPLODE = register(new ExplodeEndStellarAttack("guard", SoundEvents.GENERIC_EXPLODE));

    public static EndStellarAttackPhase register(EndStellarAttackPhase attackPhase) {
        ATTACK_PHASES.add(attackPhase);
        return attackPhase;
    }
}
