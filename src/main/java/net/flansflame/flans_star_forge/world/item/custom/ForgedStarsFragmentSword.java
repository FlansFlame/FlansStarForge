package net.flansflame.flans_star_forge.world.item.custom;

import net.flansflame.flans_knowledge_lib.tool_set.CustomSwordItem;
import net.flansflame.flans_knowledge_lib.tool_set.CustomToolSets;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ForgedStarsFragmentSword extends CustomSwordItem {

    protected static final TagKey<Block> NEEDS_THIS_TOOL = BlockTags.create(new ResourceLocation(FlansStarForge.MOD_ID,
            "needs_" + ModItems.FORGED_STARS_FRAGMENT.getId().getNamespace() + "_tool"));

    protected static final Tier TIER = TierSortingRegistry.registerTier(new ForgeTier(16, 2048, 0, 0.0F, 30, NEEDS_THIS_TOOL,
            () -> Ingredient.of(Items.NETHERITE_INGOT)), new ResourceLocation(FlansStarForge.MOD_ID,
            ModItems.FORGED_STARS_FRAGMENT.getId().getNamespace()), List.of(Tiers.NETHERITE), List.of());

    public ForgedStarsFragmentSword(int attackDamage, float attackSpeed, Properties build) {
        super(TIER, attackDamage, attackSpeed, build, new CustomToolSets.Builder().build());
    }
}