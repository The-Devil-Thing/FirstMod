package net.thedevilthing.firstmod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.thedevilthing.firstmod.Firstmod;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> INFUSABLES = createTag("infusables");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, name));
        }
    }
}
