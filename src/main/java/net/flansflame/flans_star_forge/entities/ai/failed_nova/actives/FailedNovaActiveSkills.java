package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives;

import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active.*;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class FailedNovaActiveSkills {
    public static final ArrayList<FailedNovaActiveSkill> ACTIVE_SKILLS = new ArrayList<>();

    public static final FailedNovaActiveSkill STAB = register(new FailedNovaActiveSkill("stab", false));
    public static final FailedNovaActiveSkill SUMMON_FRIENDS = register(new SummonFriendsActiveSkill("rounding_neck_with_left", SoundEvents.ENCHANTMENT_TABLE_USE, true));
    public static final FailedNovaActiveSkill EAT = register(new EatActiveSkill("knockknock", SoundEvents.WITHER_BREAK_BLOCK, true));
    public static final FailedNovaActiveSkill SHOCK_WAVE = register(new ShockWaveActiveSkill("hit", SoundEvents.WARDEN_SONIC_BOOM, true));
    public static final FailedNovaActiveSkill GRAB = register(new GrabActiveSkill("grab", SoundEvents.WITHER_BREAK_BLOCK, true));
    public static final FailedNovaActiveSkill TELEPORT = register(new TeleportActiveSkill("wing", SoundEvents.ENDERMAN_TELEPORT, true));

    private static <T extends FailedNovaActiveSkill> T register(T activeSkill) {
        ACTIVE_SKILLS.add(activeSkill);
        return activeSkill;
    }
}