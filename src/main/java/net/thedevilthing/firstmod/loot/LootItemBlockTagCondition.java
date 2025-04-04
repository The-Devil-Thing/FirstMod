package net.thedevilthing.firstmod.loot;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record LootItemBlockTagCondition(TagKey<Block> blockTag) implements LootItemCondition {
    public static final MapCodec<LootItemBlockTagCondition> CODEC = RecordCodecBuilder.<LootItemBlockTagCondition>mapCodec(inst ->
            inst.group(
            ResourceLocation.CODEC
                    .xmap(id -> TagKey.create(Registries.BLOCK, id), TagKey::location)
                    .fieldOf("tag")
                    .forGetter(LootItemBlockTagCondition::blockTag)
    ).apply(inst, LootItemBlockTagCondition::new)
    ).validate(LootItemBlockTagCondition::validate);

    private static DataResult<LootItemBlockTagCondition> validate(LootItemBlockTagCondition condition) {
        ResourceLocation id = condition.blockTag().location();
        if (id == null || id.getPath().isEmpty()) {
            return DataResult.error(() -> "Invalid or empty block tag: " + id);
        }
        return DataResult.success(condition);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.BLOCK_TAG_MATCH.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        BlockState state = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);

        return state != null && state.is(blockTag);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.BLOCK_STATE);
    }

    public static LootItemBlockTagCondition.Builder inTag (TagKey<Block> tag) {
        return new LootItemBlockTagCondition.Builder(tag);
    }

    public static class Builder implements LootItemCondition.Builder {
        private final TagKey<Block> tag;

        public Builder(TagKey<Block> tag) {
            this.tag = tag;
        }

        @Override
        public LootItemCondition build() {
            return new LootItemBlockTagCondition(tag);
        }
    }
}
