package net.thedevilthing.firstmod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thedevilthing.firstmod.Firstmod;
import net.thedevilthing.firstmod.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Firstmod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MERCURIC_DROPLETS.get());
        basicItem(ModItems.MERCURY_DROP.get());

        basicItem(ModItems.SEED.get());
    }
}
