package net.thedevilthing.firstmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thedevilthing.firstmod.block.ModBlocks;
import net.thedevilthing.firstmod.component.ModDataComponents;

import java.util.List;

public class SeedItem extends Item{
    public SeedItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static final TagKey<Item> INFUSABLES_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firstmod", "infusables"));

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        Float infuse_percent = stack.get(ModDataComponents.INFUSE_PERCENT.get());
        if (infuse_percent == null) infuse_percent = 0.0f;

        Item itemType = stack.get(ModDataComponents.ITEM_TYPE.get());
        if (itemType == null) itemType = this;

        tooltipComponents.add(Component.translatable("tooltip.firstmod.seed.infused").withStyle(ChatFormatting.DARK_GRAY));
        tooltipComponents.add(Component.translatable(itemType.getDescription().getString() + ": ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(infuse_percent + "%")));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        Player player = context.getPlayer();
        if (player == null || context.getLevel().isClientSide) return InteractionResult.PASS;

        ItemStack seedStack = context.getItemInHand();
        ItemStack offHandStack = player.getOffhandItem();

        if(state.getBlock() != ModBlocks.INFUSER.get()) {
            return InteractionResult.PASS;
        }

        float currentInfusePercent = seedStack.getOrDefault(ModDataComponents.INFUSE_PERCENT.get(), 0.0f);

        if (currentInfusePercent >= 100.0f) {
            player.displayClientMessage(Component.translatable("message.firstmod.infusion_complete").withStyle(ChatFormatting.GREEN), true);
            return InteractionResult.FAIL;
        }

        if (!offHandStack.isEmpty() && offHandStack.is(INFUSABLES_TAG)) {
            Item infusedItem = offHandStack.getItem();
            int maxIncreasePerItem = 10;
            int availableItems = offHandStack.getCount();

            // If it's a new infusion, set the item type
            if (currentInfusePercent == 0.0f) {
                seedStack.set(ModDataComponents.ITEM_TYPE.get(), infusedItem);
            } else {
                Item previousItem = seedStack.get(ModDataComponents.ITEM_TYPE.get());
                // Ensure the infusion continues with the same item type
                if (previousItem != infusedItem) {
                    assert previousItem != null;
                    player.displayClientMessage(Component.translatable("message.firstmod.wrong_item", previousItem.getDescription()).withStyle(ChatFormatting.YELLOW), true);
                    return InteractionResult.FAIL;
                }
            }

            float newInfusePercent;
            int itemsUsed = 0;

            if (player.isShiftKeyDown()) {
                float neededIncrease = 100.0f - currentInfusePercent;
                itemsUsed = (int) Math.ceil(neededIncrease / maxIncreasePerItem);
                if (itemsUsed > availableItems) {
                    itemsUsed = availableItems;
                }
                newInfusePercent = Math.min(currentInfusePercent + (itemsUsed * maxIncreasePerItem), 100.0f);
            } else {
                newInfusePercent = Math.min(currentInfusePercent + maxIncreasePerItem, 100.0f);
                itemsUsed = 1;
            }

            seedStack.set(ModDataComponents.INFUSE_PERCENT.get(), newInfusePercent);

            if (currentInfusePercent < 100.0f) {
                offHandStack.shrink(itemsUsed);
            } else {
                player.displayClientMessage(Component.translatable("message.firstmod.infusion_complete").withStyle(ChatFormatting.GREEN), true);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


}
