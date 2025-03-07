package net.thedevilthing.firstmod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Firstmod.MOD_ID);

    public static final Supplier<CreativeModeTab> FIRST_MOD_TAB =
            CREATIVE_MODE_TAB.register("first_mod_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.MERCURIC_DROPLETS.get()))
                            .title(Component.translatable("creativetab.firstmod.first_mod"))
                            .displayItems(((parameters, output) -> {
                                output.accept(ModItems.MERCURIC_DROPLETS);
                                output.accept(ModItems.MERCURY_DROP);

                                output.accept(ModItems.SEED);

                                output.accept(ModBlocks.PEDESTAL);
                                output.accept(ModBlocks.INFUSER);
                                output.accept(ModBlocks.NETHER_MERCURY_ORE);
                            }))
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
