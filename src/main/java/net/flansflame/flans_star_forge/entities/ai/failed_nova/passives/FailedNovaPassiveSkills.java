package net.flansflame.flans_star_forge.entities.ai.failed_nova.passives;

import net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.passive.ExplodeProjectilePassiveSkill;

import java.util.ArrayList;

public class FailedNovaPassiveSkills {
    public static final ArrayList<FailedNovaPassiveSkill> PASSIVE_SKILLS = new ArrayList<>();

    public static final FailedNovaPassiveSkill EXPLODE_PROJECTILE = register(ExplodeProjectilePassiveSkill.class);

    public static <T extends FailedNovaPassiveSkill> T register(Class<T> passiveSkillClass) {
        T passiveSkill;
        try {
            passiveSkill = passiveSkillClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        PASSIVE_SKILLS.add(passiveSkill);
        return passiveSkill;
    }
}
