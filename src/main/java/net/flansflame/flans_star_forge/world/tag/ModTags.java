package net.flansflame.flans_star_forge.world.tag;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ModTags {
    interface Blocks{
        //TagKey<Block> TAG_KEY = tag("tag_key");

        static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(FlansStarForge.MOD_ID, name));
        }
    }

    interface Items {
        TagKey<Item> WEAPON_WITH_BLESSING = tag("weapon_with_blessing");

        static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(FlansStarForge.MOD_ID, name));
        }
    }
}
