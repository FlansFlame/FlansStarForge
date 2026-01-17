package net.flansflame.flans_star_forge.recipes.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.recipes.recipe.CombinerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CombinerRecipeCategory implements IRecipeCategory<CombinerRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(FlansStarForge.MOD_ID, "combining");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlansStarForge.MOD_ID, "textures/gui/combiner_gui.png");
    private static final int[] TEXTURE_SIZE = {176, 85};

    public static final RecipeType<CombinerRecipe> TYPE = new RecipeType<>(UID, CombinerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CombinerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, TEXTURE_SIZE[0], TEXTURE_SIZE[1]);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COMBINER.get()));
    }

    @Override
    public RecipeType<CombinerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.flans_star_forge.combiner");
    }

    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CombinerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 15).addIngredients(recipe.getIngredient1());
        builder.addSlot(RecipeIngredientRole.INPUT, 96, 15).addIngredients(recipe.getIngredient2());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 57).addItemStack(recipe.getResultItem(null));
    }
}
