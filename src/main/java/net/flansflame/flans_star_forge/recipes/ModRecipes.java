package net.flansflame.flans_star_forge.recipes;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.recipes.recipe.CombinerRecipe;
import net.flansflame.flans_star_forge.recipes.recipe.DestructorRecipe;
import net.flansflame.flans_star_forge.recipes.recipe.ReforgerRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FlansStarForge.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CombinerRecipe>> COMBINER =
            SERIALIZER.register("combining", () -> CombinerRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<DestructorRecipe>> DESTRUCTOR =
            SERIALIZER.register("destructing", () -> DestructorRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ReforgerRecipe>> REFORGER =
            SERIALIZER.register("reforging", () -> ReforgerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }
}
