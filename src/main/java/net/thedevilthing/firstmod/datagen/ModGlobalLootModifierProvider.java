package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.item.ModItems;
import net.thedevilthing.firstmod.loot.AddItemModifier;
import net.thedevilthing.firstmod.loot.LootItemBlockTagCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Firstmod.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("mercuric_droplets_to_stone_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/stone"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_deepslate_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/deepslate"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.35f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
        this.add("mercuric_droplets_to_netherrack_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/netherrack"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.5f).build()
                }, ModItems.MERCURIC_DROPLETS.get()));
    }
}
