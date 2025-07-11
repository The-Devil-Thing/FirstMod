package net.thedevilthing.firstmod;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;
import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Firstmod.MOD_ID)
public class Config {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue TIER_COUNT = BUILDER.comment("Total Number of Tiers").defineInRange("tier_count", 4, 1, 10);
    private static final ModConfigSpec.DoubleValue BASE_INFUSION_AMOUNT = BUILDER.comment("The base insuion amount which will be multiplied by tier value.").defineInRange("base_infusion", 5, 0.1, 10);

    private static final ModConfigSpec.ConfigValue<List<? extends Integer>> TIER_VALUES = BUILDER.comment("Infusion percentage per item for each tier (Indexed from 1).").defineList("tier_values", getDefaultTiers(), Integer.class::isInstance);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int tierCount;
    public static double baseInfusion;
    public static List<Integer> tierValues;

//    private static boolean validateItemName(final Object obj) {
//        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
//    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        tierCount = TIER_COUNT.get();
        baseInfusion = BASE_INFUSION_AMOUNT.get();
        tierValues = TIER_VALUES.get().stream().map(Integer.class::cast).toList();

        if (tierValues.size() != tierCount) {
            LOGGER.error("[FirstMod] ERROR: The number of tier values ({}) does not match the tier count ({}).", tierValues.size(), tierCount);
            throw new IllegalStateException("Config Error: tier_values list size must match tier_count.");
        }
    }

    public static int getInfusionAmountForTier(int tier) {
        if (tier >= 1 && tier <= tierValues.size()) {
            return tierValues.get(tier - 1);
        }
        return 2;// Default infusion amount if tier is out of range
    }

    private static List<Integer> getDefaultTiers() {
        return List.of(1, 2, 3, 4);
    }
}
