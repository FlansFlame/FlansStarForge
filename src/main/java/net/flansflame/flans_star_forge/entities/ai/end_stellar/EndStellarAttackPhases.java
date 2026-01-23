package net.flansflame.flans_star_forge.entities.ai.end_stellar;

import net.flansflame.flans_star_forge.entities.ai.end_stellar.custom.*;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class EndStellarAttackPhases {

    public static final ArrayList<EndStellarAttackPhase> ATTACK_PHASES = new ArrayList<>();

    public static final EndStellarAttackPhase STAB = register(new StabEndStellarAttack("stab", false));
    public static final EndStellarAttackPhase SHOCK_WAVE = register(new ShockWaveEndStellarAttack("shock_wave", SoundEvents.TRIDENT_RETURN, true));
    public static final EndStellarAttackPhase SUMMON_FOLLOWER = register(new SummonFollowerEndStellarAttack("casting", SoundEvents.ENCHANTMENT_TABLE_USE, true));
    public static final EndStellarAttackPhase STAB_WITH_SPEAR = register(new EndStellarAttackPhase("stab_with_spear", false));
    public static final EndStellarAttackPhase EXPLODE = register(new ExplodeEndStellarAttack("guard", SoundEvents.GENERIC_EXPLODE, true));
    public static final EndStellarAttackPhase TELEPORT = register(new TeleportEndStellarPhase("teleport", SoundEvents.ENDERMAN_TELEPORT, true));

    public static EndStellarAttackPhase register(EndStellarAttackPhase attackPhase) {
        ATTACK_PHASES.add(attackPhase);
        return attackPhase;
    }
}
