package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Firstmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.INFUSABLES)
                .add(Items.REDSTONE)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "ingots"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "gems"))
                .remove(ResourceLocation.fromNamespaceAndPath("minecraft", "prismarine_crystals"));
    }
}
