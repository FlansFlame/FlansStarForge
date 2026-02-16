package net.flansflame.flans_star_forge.recipes;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.recipes.category.CombinerRecipeCategory;
import net.flansflame.flans_star_forge.recipes.category.DestructorRecipeCategory;
import net.flansflame.flans_star_forge.recipes.category.ReforgerRecipeCategory;
import net.flansflame.flans_star_forge.recipes.recipe.CombinerRecipe;
import net.flansflame.flans_star_forge.recipes.recipe.DestructorRecipe;
import net.flansflame.flans_star_forge.recipes.recipe.ReforgerRecipe;
import net.flansflame.flans_star_forge.screens.screen.CombinerScreen;
import net.flansflame.flans_star_forge.screens.screen.DestructorScreen;
import net.flansflame.flans_star_forge.screens.screen.ReforgerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JeiFSFPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FlansStarForge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CombinerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DestructorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ReforgerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(CombinerRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(CombinerRecipe.Type.INSTANCE));
        registration.addRecipes(DestructorRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(DestructorRecipe.Type.INSTANCE));
        registration.addRecipes(ReforgerRecipeCategory.TYPE,
                recipeManager.getAllRecipesFor(ReforgerRecipe.Type.INSTANCE));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CombinerScreen.class,
                CombinerScreen.ARROW_X, CombinerScreen.ARROW_Y,
                CombinerScreen.ARROW_WIDTH, CombinerScreen.ARROW_HEIGHT,
                CombinerRecipeCategory.TYPE
        );
        registration.addRecipeClickArea(DestructorScreen.class,
                DestructorScreen.ARROW_X, DestructorScreen.ARROW_Y,
                DestructorScreen.ARROW_WIDTH, DestructorScreen.ARROW_HEIGHT,
                DestructorRecipeCategory.TYPE
        );
        registration.addRecipeClickArea(ReforgerScreen.class,
                ReforgerScreen.ARROW_X, ReforgerScreen.ARROW_Y,
                ReforgerScreen.ARROW_WIDTH, ReforgerScreen.ARROW_HEIGHT,
                ReforgerRecipeCategory.TYPE
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(CombinerRecipeCategory.TYPE, ModBlocks.COMBINER.get());
        registration.addRecipeCatalysts(DestructorRecipeCategory.TYPE, ModBlocks.DESTRUCTOR.get());
        registration.addRecipeCatalysts(ReforgerRecipeCategory.TYPE, ModBlocks.REFORGER.get());
    }
}
