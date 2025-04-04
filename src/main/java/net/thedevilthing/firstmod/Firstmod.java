package net.thedevilthing.firstmod;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.thedevilthing.firstmod.block.ModBlocks;
import net.thedevilthing.firstmod.component.ModDataComponents;
import net.thedevilthing.firstmod.item.ModCreativeModeTabs;
import net.thedevilthing.firstmod.item.ModItems;
import net.thedevilthing.firstmod.loot.ModLootConditions;
import net.thedevilthing.firstmod.loot.ModLootModifiers;
import net.thedevilthing.firstmod.util.datapack.DataPackGenerator;
import net.thedevilthing.firstmod.util.datapack.DataPackManager;
import net.thedevilthing.firstmod.util.datapack.DynamicTagGenerator;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Firstmod.MOD_ID)
public class Firstmod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "firstmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Firstmod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Firstmod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModDataComponents.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModLootConditions.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "firstmod/firstmod-common.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, InfusionItemsConfig.SPEC, "firstmod/firstmod_infusion_items.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();

        File dataPackFolder = DataPackManager.getDataPackFolder(server);

        DataPackGenerator.generateDataPack(dataPackFolder);
        DynamicTagGenerator.generateTagFile(dataPackFolder);

        LOGGER.debug("DataPack Generation Complete {}", dataPackFolder.getAbsolutePath());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
