package net.flansflame.flans_star_forge.entities.ai.failed_nova.actives;

import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active.EatActiveSkill;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.actives.active.SummonFriendsActiveSkill;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class FailedNovaActiveSkills {
    public static final ArrayList<FailedNovaActiveSkill> ACTIVE_SKILLS = new ArrayList<>();

    public static final FailedNovaActiveSkill STAB = register(new FailedNovaActiveSkill("stab", false));
    public static final FailedNovaActiveSkill SUMMON_FRIENDS = register(new SummonFriendsActiveSkill("rounding_neck_with_left", SoundEvents.ENCHANTMENT_TABLE_USE, true));
    public static final FailedNovaActiveSkill EAT = register(new EatActiveSkill("knockknock", SoundEvents.WITHER_BREAK_BLOCK, true));

    private static <T extends FailedNovaActiveSkill> T register(T activeSkill) {
        ACTIVE_SKILLS.add(activeSkill);
        return activeSkill;
    }
}