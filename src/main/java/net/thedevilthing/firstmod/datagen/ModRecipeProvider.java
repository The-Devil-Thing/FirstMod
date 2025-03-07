package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.block.ModBlocks;
import net.thedevilthing.firstmod.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PEDESTAL.get())
                .pattern("HSH")
                .pattern("HMH")
                .pattern("HSH")
                .define('H', Items.STONE_SLAB)
                .define('S', Items.STONE)
                .define('M', ModItems.MERCURY_DROP.get())
                .unlockedBy("has_mercury_drop", has(ModItems.MERCURY_DROP.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFUSER.get())
                .pattern("III")
                .pattern("MRM")
                .pattern("III")
                .define('I', Items.IRON_BLOCK)
                .define('M', ModItems.MERCURY_DROP.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_mercury_drop", has(ModItems.MERCURY_DROP.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MERCURY_DROP.get())
                .pattern("MM")
                .pattern("MM")
                .define('M', ModItems.MERCURIC_DROPLETS.get())
                .unlockedBy("has_mercuric_droplets", has(ModItems.MERCURIC_DROPLETS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MERCURIC_DROPLETS.get(), 4)
                .requires(ModItems.MERCURY_DROP.get())
                .unlockedBy("has_mercury_drop", has(ModItems.MERCURY_DROP.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SEED.get())
                .pattern(" M ")
                .pattern("MIM")
                .pattern(" M ")
                .define('M', ModItems.MERCURIC_DROPLETS.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_mercuric_droplets", has(ModItems.MERCURIC_DROPLETS.get()))
                .save(recipeOutput);

        oreSmelting(recipeOutput, List.of(ModBlocks.NETHER_MERCURY_ORE.get()), RecipeCategory.MISC, ModItems.MERCURIC_DROPLETS.get(), 0.7F, 200, "mercury_droplets");

        oreBlasting(recipeOutput, List.of(ModBlocks.NETHER_MERCURY_ORE.get()), RecipeCategory.MISC, ModItems.MERCURIC_DROPLETS.get(), 0.7F, 100, "mercury_droplets");
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Firstmod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
