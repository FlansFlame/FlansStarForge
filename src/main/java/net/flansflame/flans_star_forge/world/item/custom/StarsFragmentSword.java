package net.flansflame.flans_star_forge.world.item.custom;

import net.flansflame.flans_knowledge_lib.tool_set.CustomSwordItem;
import net.flansflame.flans_knowledge_lib.tool_set.CustomToolSets;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.component.ModComponentTags;
import net.flansflame.flans_star_forge.world.effect.ModEffects;
import net.flansflame.flans_star_forge.world.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class StarsFragmentSword extends CustomSwordItem {

    protected static final TagKey<Block> NEEDS_THIS_TOOL = BlockTags.create(new ResourceLocation(FlansStarForge.MOD_ID,
            "needs_" + ModItems.STARS_FRAGMENT.getId().getPath() + "_tool"));

    protected static final Tier TIER = TierSortingRegistry.registerTier(new ForgeTier(16, 8192, 0, 0.0F, 32, NEEDS_THIS_TOOL,
            () -> Ingredient.of(Items.NETHERITE_INGOT)), new ResourceLocation(
                    ModItems.STARS_FRAGMENT.getId().toString()), List.of(ForgedStarsFragmentSword.TIER), List.of());

    public StarsFragmentSword(int attackDamage, float attackSpeed, Properties build) {
        super(TIER, attackDamage, attackSpeed, build, new CustomToolSets.Builder().build());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND){
            ModComponentTags.TOGGLE_BLESSING.trigger(player.getItemInHand(hand));
            player.playSound(ModComponentTags.TOGGLE_BLESSING.get(player.getItemInHand(hand)) ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE);

            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof Player player && ModComponentTags.TOGGLE_BLESSING.get(itemStack) && !player.level().isClientSide){
            player.addEffect(new MobEffectInstance(ModEffects.STARS_BLESSING.get(), 10, 0, true, true));
        }
        super.inventoryTick(itemStack, level, entity, slot, selected);
    }
}