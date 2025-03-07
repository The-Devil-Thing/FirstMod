package net.thedevilthing.firstmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Firstmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.NETHER_MERCURY_ORE.get())
                .add(ModBlocks.PEDESTAL.get())
                .add(ModBlocks.INFUSER.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.NETHER_MERCURY_ORE.get());
    }
}
