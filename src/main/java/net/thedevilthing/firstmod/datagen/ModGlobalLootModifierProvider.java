package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.loot.AddItemModifier;
import net.thedevilthing.firstmod.loot.LootItemBlockTagCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Firstmod.MOD_ID);
    }

    @Override
    protected void start() {
        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);


//        System.out.println("=== Enchantments in Registry ===");
//        enchantments.listElements().forEach(holder -> {
//            System.out.println("- " + holder.key().location());
//        });
//        System.out.println("===============================");


        Holder<Enchantment> mercuryFilter = enchantments.getOrThrow(Enchantments.FORTUNE);

        this.add("mercuric_droplets_to_stone_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/stone"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build(),
                }, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, "inject/mercuric_droplets")), mercuryFilter));
        this.add("mercuric_droplets_to_deepslate_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/deepslate"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.35f).build()
                }, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, "inject/mercuric_droplets")), mercuryFilter));
        this.add("mercuric_droplets_to_netherrack_ores",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagCondition.inTag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","ores_in_ground/netherrack"))).build(),
                        LootItemRandomChanceCondition.randomChance(0.5f).build()
                }, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, "inject/mercuric_droplets")), mercuryFilter));
    }
}
