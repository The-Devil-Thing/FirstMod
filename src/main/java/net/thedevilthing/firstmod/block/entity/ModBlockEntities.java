package net.thedevilthing.firstmod.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Firstmod.MOD_ID);

    public static final Supplier<BlockEntityType<InfuserBlockEntity>> INFUSER_BE =
            BLOCK_ENTITIES.register("infuser_be", () -> BlockEntityType.Builder.of(
                    InfuserBlockEntity::new, ModBlocks.INFUSER.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
