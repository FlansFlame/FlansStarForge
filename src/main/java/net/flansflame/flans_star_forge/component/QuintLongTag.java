package net.flansflame.flans_star_forge.component;

import net.flansflame.flans_knowledge_lib.component.Tags;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class QuintLongTag extends Tags {
    public QuintLongTag(String id) {
        super(id);
    }

    public QuintLong get(ItemStack itemStack) {
        long[] layers = new long[QuintLong.LAYER_SIZE];

        for (int i = 0; i < layers.length; i++) {
            layers[i] = itemStack.getOrCreateTag().getLong(this.MOD_ID + ":" + this.ID + "_layer" + i);
        }

        return QuintLong.createFromList(layers);
    }

    public void set(ItemStack itemStack, QuintLong quintLong) {
        long[] layers = quintLong.getLayer();

        for (int i = 0; i < layers.length; i++) {
            itemStack.getOrCreateTag().putLong(this.MOD_ID + ":" + this.ID + "_layer" + i, layers[i]);
        }
    }

    public void set(ItemStack itemStack){
        this.set(itemStack, new QuintLong(0));
    }

    public void getAndSet(ItemStack itemStack, Function<QuintLong, QuintLong> quintLong) {
        this.set(itemStack, quintLong.apply(this.get(itemStack)));
    }
}
