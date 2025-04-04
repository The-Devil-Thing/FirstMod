package net.thedevilthing.firstmod.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.thedevilthing.firstmod.Firstmod;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> MERCURY_FILTER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, "mercury_filter"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup((Registries.ITEM));

        register(context, MERCURY_FILTER, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                items.getOrThrow(ItemTags.PICKAXES),
                5,
                5,
                Enchantment.dynamicCost(7, 7),
                Enchantment.dynamicCost(13,7),
                2,
                EquipmentSlotGroup.MAINHAND
        )));
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
