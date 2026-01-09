package net.flansflame.flans_star_forge.world.ai.failed_nova.active;

import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;

public class FailedNovaActiveSkills {
    public static final ArrayList<FailedNovaActiveSkill> ACTIVE_SKILLS = new ArrayList<>();

    public static final FailedNovaActiveSkill STAB = register(new FailedNovaActiveSkill("stab", true));

    private static <T extends FailedNovaActiveSkill> T register(T activeSkill) {
        ACTIVE_SKILLS.add(activeSkill);
        return activeSkill;
    }
}
