package net.thedevilthing.firstmod.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedevilthing.firstmod.Firstmod;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), Firstmod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> INFUSE_PERCENT = register("infuse_percent", builder -> builder.persistent(Codec.FLOAT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Item>> ITEM_TYPE = register("item_type", builder -> builder.persistent(
            BuiltInRegistries.ITEM.byNameCodec()
    ));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
