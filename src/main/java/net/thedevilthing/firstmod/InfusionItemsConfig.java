package net.thedevilthing.firstmod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Firstmod.MOD_ID)
public class InfusionItemsConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_TIERS = BUILDER.comment("Item to Tiers mapping.")
            .defineList("item_tiers", getDefaultMappings(), String.class::isInstance);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static List<String> itemTiers = List.of();

    private static List<String> getDefaultMappings() {
        return List.of(
                "minecraft:coal=1",
                "minecraft:iron_ingot=1",
                "minecraft:copper_ingot=1",
                "minecraft:lapis_lazuli=1",
                "minecraft:amethyst_shard=2",
                "minecraft:redstone=2",
                "minecraft:quartz=2",
                "minecraft:gold_ingot=2",
                "minecraft:diamond=3",
                "minecraft:emerald=3",
                "minecraft:netherite_ingot=4"
        );
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            itemTiers = ITEM_TIERS.get().stream().map(String.class::cast).toList();
        }
    }

    public static int getItemTier(String itemID) {
        return itemTiers.stream()
                .map(entry -> entry.split("="))
                .filter(parts -> parts.length == 2 && parts[0].equals(itemID))
                .map(parts -> Integer.parseInt(parts[1]))
                .findFirst()
                .orElse(-1);
    }

    public static Set<String> getAllItems() {
        return itemTiers.stream()
                .map(entry -> entry.split("=")[0]) // Extract item name from "item=tier"
                .collect(Collectors.toSet());
    }
}
