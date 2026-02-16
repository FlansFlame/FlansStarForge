package net.flansflame.flans_star_forge.items.item;

import net.flansflame.flans_star_forge.component.ModComponentTags;
import net.flansflame.flans_star_forge.component.QuintLongTag;
import net.flansflame.flans_star_forge.energy.QuintLong;
import net.flansflame.flans_star_forge.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergizedClockItem extends Item {

    private static final int MAX_BAR_LENGTH = 13;

    public EnergizedClockItem(Properties build) {
        super(build);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {

        component.add(Component.translatable("item.flans_star_forge.energized_clock.desc1", getStoredEnergy(itemStack)).withStyle(ChatFormatting.DARK_GRAY));
        component.add(Component.translatable("item.flans_star_forge.energized_clock.desc2", this.getCapacity()).withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(itemStack, level, component, flag);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return !getStoredEnergy(itemStack).is(this.getCapacity());
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return getStoredEnergy(itemStack).is(this.getCapacity());
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return (int) (MAX_BAR_LENGTH * getStoredEnergy(itemStack).divideAndGetFloat(this.getCapacity()));
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return 4194168;
    }

    public QuintLong getCapacity() {
        return new QuintLong(100000);
    }

    public static QuintLong getStoredEnergy(ItemStack itemStack) {
        return getComponentTag().get(itemStack);
    }

    public static QuintLongTag getComponentTag() {
        return ModComponentTags.STORED_ENERGY;
    }

    public static QuintLong getSpace(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EnergizedClockItem clock) {
            return clock.getCapacity().remove(getStoredEnergy(itemStack));
        }

        return new QuintLong();
    }

    public static QuintLong send(ItemStack itemStack, QuintLong send) {

        if (send.isSmallerThan(0)) return new QuintLong();

        if (itemStack.getItem() instanceof EnergizedClockItem) {

            QuintLong space = getSpace(itemStack);

            if (send.isGreaterThan(space)) send.set(space);

            getComponentTag().getAndSet(itemStack, quintLong -> quintLong.add(send));
            return send;
        }
        return new QuintLong();
    }

    public static QuintLong drain(ItemStack itemStack, QuintLong drain) {

        if (drain.isSmallerThan(0)) return new QuintLong();

        if (itemStack.getItem() instanceof EnergizedClockItem) {

            QuintLong energy = getStoredEnergy(itemStack);

            if (drain.isGreaterThan(energy)) drain.set(energy);

            getComponentTag().getAndSet(itemStack, quintLong -> quintLong.remove(drain));
            return drain;
        }
        return new QuintLong();
    }

    public static ItemStack getFilledClock(){
        ItemStack itemStack = new ItemStack(ModItems.ENERGIZED_CLOCK.get());
        ModComponentTags.STORED_ENERGY.set(itemStack, ((EnergizedClockItem) ModItems.ENERGIZED_CLOCK.get()).getCapacity());
        return itemStack;
    }
}
