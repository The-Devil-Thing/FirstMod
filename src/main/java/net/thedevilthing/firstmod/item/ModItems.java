package net.thedevilthing.firstmod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.item.custom.SeedItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Firstmod.MOD_ID);

    public static final DeferredItem<Item> MERCURIC_DROPLETS = ITEMS.register("mercuric_droplets",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MERCURY_DROP = ITEMS.register("mercury_drop",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SEED = ITEMS.register("seed",
            () -> new SeedItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
