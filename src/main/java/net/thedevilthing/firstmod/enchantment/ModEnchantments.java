package net.thedevilthing.firstmod.enchantment;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.thedevilthing.firstmod.Firstmod;

import java.util.List;
import java.util.Optional;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> MERCURY_FILTER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, "mercury_filter"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup((Registries.ITEM));

        register(context, MERCURY_FILTER,
                  new Enchantment(
                          Component.translatable("enchantment.firstmod.mercury_filter"),
                          new Enchantment.EnchantmentDefinition(
                                  items.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                                  Optional.of(items.getOrThrow(ItemTags.PICKAXES)),
                                  5,
                                  5,
                                  new Enchantment.Cost(7, 7),
                                  new Enchantment.Cost(13, 7),
                                  2,
                                  List.of(EquipmentSlotGroup.MAINHAND)
                          ),
                          HolderSet.empty(),
                          DataComponentMap.builder()
                                  .build()
                  )
                );

        System.out.println("Enchantment Bootstrap Called :  " + MERCURY_FILTER.location());
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment enchantment) {
        registry.register(key, enchantment);
    }
}
