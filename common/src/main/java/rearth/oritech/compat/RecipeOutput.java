package rearth.oritech.compat;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Compatibility layer for MC 1.21's RecipeOutput.
 * In MC 1.20.1, we use Consumer<FinishedRecipe> instead.
 */
public interface RecipeOutput extends Consumer<FinishedRecipe> {
    
    /**
     * Accept a recipe with an associated advancement.
     */
    default void accept(ResourceLocation id, FinishedRecipe recipe, Advancement.Builder advancement) {
        accept(recipe);
    }
    
    /**
     * Wrap a Consumer<FinishedRecipe> as a RecipeOutput.
     */
    static RecipeOutput wrap(Consumer<FinishedRecipe> consumer) {
        return consumer::accept;
    }
}
