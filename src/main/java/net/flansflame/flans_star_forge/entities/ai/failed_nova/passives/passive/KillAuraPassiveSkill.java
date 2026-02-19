package net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.passive;

import net.flansflame.flans_knowledge_lib.Utils;
import net.flansflame.flans_star_forge.damagesource.ModDamageTypes;
import net.flansflame.flans_star_forge.effects.ModEffects;
import net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.FailedNovaPassiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class KillAuraPassiveSkill extends FailedNovaPassiveSkill {

    public static int KILL_AURA_USE_TIME = 20;

    @Override
    public void onPassiveSkill(FailedNovaEntity nova, Entity entity) {
        if (entity instanceof Player player) {
            if (!player.hasEffect(ModEffects.STARS_BLESSING.get()) && nova.tickCount % KILL_AURA_USE_TIME == 0 && player.level() instanceof ServerLevel server) {
                player.hurt(Utils.createDamageSource(server, ModDamageTypes.MAGIC_WITH_ALL_BYPASS), player.getMaxHealth() >= 20f ? player.getMaxHealth() / 10f : 2f);
            }
        }
    }
}