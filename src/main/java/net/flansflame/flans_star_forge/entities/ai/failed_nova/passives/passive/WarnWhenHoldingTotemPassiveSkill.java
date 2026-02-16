package net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.passive;

import net.flansflame.flans_star_forge.entities.ai.failed_nova.passives.FailedNovaPassiveSkill;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class WarnWhenHoldingTotemPassiveSkill extends FailedNovaPassiveSkill {
    @Override
    public void onPassiveSkill(FailedNovaEntity nova, Entity entity) {
        if (entity instanceof Player player && (player.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) && player.level().isClientSide){
            player.displayClientMessage(Component.translatable("text.flans_star_forge.warn_totem"), true);
        }
    }
}
