package net.thedevilthing.firstmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thedevilthing.firstmod.Config;
import net.thedevilthing.firstmod.InfusionItemsConfig;
import net.thedevilthing.firstmod.component.ModDataComponents;
import net.thedevilthing.firstmod.item.ModItems;
import net.thedevilthing.firstmod.screen.custom.InfuserMenu;
import org.jetbrains.annotations.Nullable;

public class InfuserBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            if (slot == 1) {
                return 1;
            } else {
                return super.getStackLimit(slot, stack);
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int RESOURCE_SLOT = 0;
    private static final int SEED_SLOT = 1;
    public static final TagKey<Item> INFUSABLES_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firstmod", "infusables"));

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    private static final DataComponentType<Float> INFUSE_PERCENT = ModDataComponents.INFUSE_PERCENT.get();
    private static final DataComponentType<Item> ITEM_TYPE = ModDataComponents.ITEM_TYPE.get();
    private static final DataComponentType<Integer> TIER = ModDataComponents.TIER.get();

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.INFUSER_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> InfuserBlockEntity.this.progress;
                    case 1 -> InfuserBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: InfuserBlockEntity.this.progress = value;
                    case 1: InfuserBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    private float rotation;

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.infuser");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new InfuserMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("infuser.progress", progress);
        tag.putInt("infuser.max_progress", maxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("infuser.progress");
        maxProgress = tag.getInt("infuser.max_progress");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        ItemStack resourceStack = inventory.getStackInSlot(RESOURCE_SLOT);
        ItemStack seedStack = inventory.getStackInSlot(SEED_SLOT);
        float currentInfusePercent = seedStack.getOrDefault(INFUSE_PERCENT, 0.0f);
        Item resourceItem = resourceStack.getItem();
        String itemID = BuiltInRegistries.ITEM.getKey(resourceItem).toString();
        int itemTier = InfusionItemsConfig.getItemTier(itemID);
        ItemStack newSeedStack = seedStack.copy();

        newSeedStack.set(ITEM_TYPE, resourceItem);

        int availableItems = resourceStack.getCount();
        int tierInfusionAmount = Config.getInfusionAmountForTier(itemTier);
        double baseInfusePercent = Config.baseInfusion;
        int tierInfusePercent = (int) Math.ceil(tierInfusionAmount * baseInfusePercent);
        float neededIncrease = 100.0f - currentInfusePercent;
        int itemsUsed = (int) Math.ceil(neededIncrease / tierInfusePercent);
        if (itemsUsed > availableItems) {
            itemsUsed = availableItems;
        }
        float newInfusePercent = Math.min(currentInfusePercent + (itemsUsed * tierInfusePercent), 100.0f);

        newSeedStack.set(INFUSE_PERCENT, newInfusePercent);

        inventory.extractItem(RESOURCE_SLOT, itemsUsed, false);
        inventory.setStackInSlot(SEED_SLOT, newSeedStack);
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        ItemStack resourceStack = inventory.getStackInSlot(RESOURCE_SLOT);
        ItemStack seedStack = inventory.getStackInSlot(SEED_SLOT);

        if (resourceStack.isEmpty() || seedStack.isEmpty()) {
            return false;
        }

        if (!resourceStack.is(INFUSABLES_TAG)) {
            return false;
        }
        
        if (seedStack.getItem() != ModItems.SEED.get()) {
            return false;
        }

        Item resourceItem = resourceStack.getItem();
        String itemID = BuiltInRegistries.ITEM.getKey(resourceItem).toString();
        int itemTier = InfusionItemsConfig.getItemTier(itemID);
        int seedTier = seedStack.getOrDefault(TIER, 1);

        if (itemTier > seedTier) {
            return false;
        }

        Item infusedItem = seedStack.get(ITEM_TYPE);
        float currentInfusePercent = seedStack.getOrDefault(INFUSE_PERCENT, 0.0f);

        if (currentInfusePercent >= 100.0f) {
            return false;
        }

        return currentInfusePercent == 0.0f || (currentInfusePercent > 0.0f && (resourceItem == infusedItem));
    }


    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
