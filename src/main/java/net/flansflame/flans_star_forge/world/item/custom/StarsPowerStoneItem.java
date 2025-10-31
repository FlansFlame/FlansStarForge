package net.flansflame.flans_star_forge.world.item.custom;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.componet.ModComponentTags;
import net.flansflame.flans_star_forge.world.entity.ModEntities;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StarsPowerStoneItem extends Item {
    public StarsPowerStoneItem(Properties build) {
        super(build);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return !ModComponentTags.OWNER_UUID.get(itemStack).isEmpty();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level level = player.level();

        ItemStack itemStack = player.getItemInHand(hand);

        if (ModComponentTags.OWNER_UUID.get(itemStack).equals(player.getStringUUID())) {
            if (level instanceof ServerLevel server) {
                StellarEntity entityToSpawn = ModEntities.STELLAR.get().spawn(server, player.getOnPos().above(), MobSpawnType.COMMAND);
                if (entityToSpawn != null) {
                    entityToSpawn.tame(player);
                    entityToSpawn.setYRot(server.getRandom().nextFloat() * 360F);
                    entityToSpawn.setSitting(true);
                }
            }

            if (level.isClientSide){
                player.displayClientMessage(Component.translatable("flanaf-bau." + FlansStarForge.MOD_ID + ".stellar_on_spawn", player.getName()), false);
            }

            ModComponentTags.OWNER_UUID.set(itemStack);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
