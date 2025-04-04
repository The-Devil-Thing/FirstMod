package net.thedevilthing.firstmod.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedevilthing.firstmod.Firstmod;

import java.util.function.Supplier;

public class ModLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Firstmod.MOD_ID);

    public static final Supplier<LootItemConditionType> BLOCK_TAG_MATCH =
            LOOT_CONDITIONS.register("block_tag_match",
                    () -> new LootItemConditionType(LootItemBlockTagCondition.CODEC));

    public static void register(IEventBus eventBus) {
        LOOT_CONDITIONS.register(eventBus);
    }
}
