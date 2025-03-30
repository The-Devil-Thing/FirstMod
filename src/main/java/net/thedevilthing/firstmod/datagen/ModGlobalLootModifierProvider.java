package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.item.ModItems;
import net.thedevilthing.firstmod.loot.AddItemModifier;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Firstmod.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("mercuric_droplets_to_copper_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.COPPER_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_deepslate_copper_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_COPPER_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.10f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_iron_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.IRON_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_deepslate_iron_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_IRON_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.20f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_gold_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GOLD_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.25f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_deepslate_gold_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_GOLD_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.30f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_diamond_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DIAMOND_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.45f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_deepslate_diamond_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_DIAMOND_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.50f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_quartz_ore",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.NETHER_QUARTZ_ORE).build(),
                        LootItemRandomChanceCondition.randomChance(0.60f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
    }
}
