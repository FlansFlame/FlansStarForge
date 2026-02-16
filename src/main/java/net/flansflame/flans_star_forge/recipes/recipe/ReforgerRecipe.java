package net.flansflame.flans_star_forge.recipes.recipe;

import com.google.gson.JsonObject;
import net.flansflame.flans_star_forge.blocks.entity.ReforgerBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ReforgerRecipe implements Recipe<SimpleContainer> {

    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation id;

    public ReforgerRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide) return false;

        return input.test(container.getItem(ReforgerBlockEntity.INPUT_SLOT));
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

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ReforgerRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ReforgerRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<ReforgerRecipe> {
        private Type() {
        }

        public static final ReforgerRecipe.Type INSTANCE = new ReforgerRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<ReforgerRecipe> {
        public static final ReforgerRecipe.Serializer INSTANCE = new ReforgerRecipe.Serializer();

        @Override
        public ReforgerRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new ReforgerRecipe(id, input, output);
        }

        @Override
        public ReforgerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();

            return new ReforgerRecipe(id, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ReforgerRecipe recipe) {
            recipe.getInput().toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
