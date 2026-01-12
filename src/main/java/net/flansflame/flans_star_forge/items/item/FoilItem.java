package net.flansflame.flans_star_forge.items.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FoilItem extends Item {
    public FoilItem(Properties build) {
        super(build);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
