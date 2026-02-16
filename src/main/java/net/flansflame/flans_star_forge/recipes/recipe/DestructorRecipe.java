package net.flansflame.flans_star_forge.recipes.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.flansflame.flans_star_forge.blocks.entity.DestructorBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class DestructorRecipe implements Recipe<SimpleContainer> {

    private final Ingredient input;
    private final ItemStack output;
    private final ItemStack subOutput;
    private final FluidStack fluidOutput;
    private final ResourceLocation id;

    public DestructorRecipe(ResourceLocation id, Ingredient input, ItemStack output, ItemStack subOutput, FluidStack fluidOutput) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.subOutput = subOutput;
        this.fluidOutput = fluidOutput;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide) return false;

        return input.test(container.getItem(DestructorBlockEntity.INPUT_SLOT));
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registry) {
        return output.copy();
    }

    public ItemStack getSubResultItem(RegistryAccess registry) {
        return subOutput.copy();
    }

    public FluidStack getFluidResultItem(RegistryAccess registry) {
        return fluidOutput.copy();
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DestructorRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return DestructorRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<DestructorRecipe> {
        private Type() {
        }

        public static final DestructorRecipe.Type INSTANCE = new DestructorRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<DestructorRecipe> {
        public static final DestructorRecipe.Serializer INSTANCE = new DestructorRecipe.Serializer();

        @Override
        public DestructorRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            ItemStack subOutput = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "side_result"));

            FluidStack fluidOutput = null;
            JsonObject fluidJson = json.getAsJsonObject("fluid_result");
            JsonElement fluid = fluidJson.get("fluid");
            JsonElement fluidAmount = fluidJson.get("amount");
            if (fluid != null && fluidAmount != null) {
                fluidOutput = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluid.getAsString()))), fluidAmount.getAsInt());
            }

            return new DestructorRecipe(id, input, output, subOutput, fluidOutput);
        }

        @Override
        public DestructorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            ItemStack subOutput = buf.readItem();
            FluidStack fluidOutput = buf.readFluidStack();

            return new DestructorRecipe(id, input, output, subOutput, fluidOutput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DestructorRecipe recipe) {
            recipe.getInput().toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(null), false);
            buf.writeItemStack(recipe.getSubResultItem(null), false);
            buf.writeFluidStack(recipe.getFluidResultItem(null));
        }
    }
}
