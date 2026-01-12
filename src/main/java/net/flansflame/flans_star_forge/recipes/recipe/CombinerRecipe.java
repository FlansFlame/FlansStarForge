package net.flansflame.flans_star_forge.recipes.recipe;

import com.google.gson.JsonObject;
import net.flansflame.flans_star_forge.blocks.entity.CombinerBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CombinerRecipe implements Recipe<SimpleContainer> {

    private final Ingredient input1;
    private final Ingredient input2;
    private final ItemStack output;
    private final ResourceLocation id;

    public CombinerRecipe(ResourceLocation id, Ingredient input1, Ingredient input2, ItemStack output) {
        this.id = id;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide) return false;

        return input1.test(container.getItem(CombinerBlockEntity.INPUT_1_SLOT)) && input2.test(container.getItem(CombinerBlockEntity.INPUT_2_SLOT));
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registry) {
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

    public Ingredient getIngredient1() {
        return this.input1;
    }

    public Ingredient getIngredient2() {
        return this.input2;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CombinerRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<CombinerRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public CombinerRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient_a"));
            Ingredient input2 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient_b"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new CombinerRecipe(id, input1, input2, output);
        }

        @Override
        public CombinerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input1 = Ingredient.fromNetwork(buf);
            Ingredient input2 = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();

            return new CombinerRecipe(id, input1, input2, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CombinerRecipe recipe) {
            recipe.getIngredient1().toNetwork(buf);
            recipe.getIngredient2().toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
