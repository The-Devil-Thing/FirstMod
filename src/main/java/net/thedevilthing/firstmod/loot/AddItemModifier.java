package net.thedevilthing.firstmod.loot;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.slf4j.Logger;

public class AddItemModifier extends AddTableLootModifier {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Holder<Enchantment> enchantment;

    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions),
                    ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(AddItemModifier::table),
                    RegistryFixedCodec.create(Registries.ENCHANTMENT).fieldOf("enchantment")
                            .forGetter(AddItemModifier::getEnchantment)
            ).apply(inst, AddItemModifier::new));

    public AddItemModifier(LootItemCondition[] conditionsIn, ResourceKey<LootTable> table, Holder<Enchantment> enchantment) {
        super(conditionsIn, table);
        this.enchantment = enchantment;
    }

    public Holder<Enchantment> getEnchantment() {
        return this.enchantment;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int rolls = 1;
        int enchantmentLevel = 0;

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            enchantmentLevel = tool.getEnchantmentLevel(enchantment);
        }

        int finalRolls = rolls + enchantmentLevel;

        LOGGER.debug("Number of times the loot table will be rolled: {}", finalRolls);

        context.getResolver().get(Registries.LOOT_TABLE, table()).ifPresent(extraTable -> {
            for (int i = 0; i < finalRolls; i++) {
                extraTable.value().getRandomItems(context, generatedLoot::add);
            }
        });

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
