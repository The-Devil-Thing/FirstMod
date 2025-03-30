package net.thedevilthing.firstmod.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.thedevilthing.firstmod.Firstmod;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> NETHER_MERCURY_ORE_PLACEED_KEY = registerKey("nether_mercury_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, NETHER_MERCURY_ORE_PLACEED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_MERCURY_ORE_KEY),
                ModOrePlacement.rareOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.absolute(121), VerticalAnchor.absolute(75))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Firstmod.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
