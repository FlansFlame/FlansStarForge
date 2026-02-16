package net.flansflame.flans_star_forge.items.item;

import net.flansflame.flans_star_forge.component.ModComponentTags;
import net.flansflame.flans_star_forge.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PressurizedClockItem extends Item {

    private static final int MAX_BAR_LENGTH = 13;

    public PressurizedClockItem(Properties build) {
        super(build);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {

        component.add(getStoredFluidType(itemStack).getFluidType().getDescription().copy().withStyle(ChatFormatting.DARK_GRAY));
        component.add(Component.translatable("item.flans_star_forge.pressurized_clock.desc1", getStoredAmount(itemStack)).withStyle(ChatFormatting.DARK_GRAY));
        component.add(Component.translatable("item.flans_star_forge.pressurized_clock.desc2", this.getCapacity()).withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(itemStack, level, component, flag);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return getStoredAmount(itemStack) != this.getCapacity();
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return getStoredAmount(itemStack) == this.getCapacity();
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return (int) (MAX_BAR_LENGTH * (getStoredAmount(itemStack) / (float) this.getCapacity()));
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return 4292095;
    }

    public int getCapacity() {
        return 10000;
    }

    public static FluidStack getStoredFluid(ItemStack itemStack) {
        return new FluidStack(getStoredFluidType(itemStack), getStoredAmount(itemStack));
    }

    public static Fluid getStoredFluidType(ItemStack itemStack) {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(ModComponentTags.STORED_FLUID_TYPE.get(itemStack)));
    }

    public static int getStoredAmount(ItemStack itemStack) {
        return ModComponentTags.STORED_FLUID_AMOUNT.get(itemStack);
    }

    public static int getSpace(ItemStack itemStack) {
        if (itemStack.getItem() instanceof PressurizedClockItem clock) {
            return clock.getCapacity() - getStoredAmount(itemStack);
        }
        return 0;
    }

    public static int send(ItemStack itemStack, FluidStack fluidStack) {
        if (fluidStack.getAmount() < 0) return 0;

        if (itemStack.getItem() instanceof PressurizedClockItem) {
            if (fluidStack.getFluid() != getStoredFluidType(itemStack) && !getStoredFluid(itemStack).isEmpty())
                return 0;

            int sendAmount = fluidStack.getAmount();
            int space = getSpace(itemStack);

            if (sendAmount > space) sendAmount = space;

            ModComponentTags.STORED_FLUID_AMOUNT.add(itemStack, sendAmount);
            ModComponentTags.STORED_FLUID_TYPE.set(itemStack, String.valueOf(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())));
            return sendAmount;
        }
        return 0;
    }

    public static int drain(ItemStack itemStack, FluidStack fluidStack) {
        if (fluidStack.getAmount() < 0) return 0;

        if (itemStack.getItem() instanceof PressurizedClockItem) {
            if (fluidStack.getFluid() != getStoredFluidType(itemStack)) return 0;

            int drain = fluidStack.getAmount();
            int storedAmount = getStoredAmount(itemStack);

            if (drain > storedAmount) drain = storedAmount;

            ModComponentTags.STORED_FLUID_AMOUNT.remove(itemStack);

            if (drain == storedAmount) {
                ModComponentTags.STORED_FLUID_TYPE.set(itemStack, "");
            }
            return drain;
        }
        return 0;
    }

    public static ItemStack getFilledClock(Fluid fluid){
        ItemStack itemStack = new ItemStack(ModItems.PRESSURIZED_CLOCK.get());
        ModComponentTags.STORED_FLUID_TYPE.set(itemStack, String.valueOf(ForgeRegistries.FLUIDS.getKey(fluid)));
        ModComponentTags.STORED_FLUID_AMOUNT.set(itemStack, ((PressurizedClockItem) ModItems.PRESSURIZED_CLOCK.get()).getCapacity());
        return itemStack;
    }
}
