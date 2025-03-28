package net.thedevilthing.firstmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.thedevilthing.firstmod.Config;
import net.thedevilthing.firstmod.InfusionItemsConfig;
import net.thedevilthing.firstmod.block.ModBlocks;
import net.thedevilthing.firstmod.component.ModDataComponents;

import java.util.List;

public class SeedItem extends Item{

    public SeedItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static final TagKey<Item> INFUSABLES_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firstmod", "infusables"));

    private static final DataComponentType<Float> INFUSE_PERCENT = ModDataComponents.INFUSE_PERCENT.get();
    private static final DataComponentType<Item> ITEM_TYPE = ModDataComponents.ITEM_TYPE.get();
    private static final DataComponentType<Integer> TIER = ModDataComponents.TIER.get();

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        Float infuse_percent = stack.getOrDefault(INFUSE_PERCENT, 0.0f);
        Item itemType = stack.getOrDefault(ITEM_TYPE, this);
        int tier = stack.getOrDefault(TIER, 1);

        tooltipComponents.add(Component.translatable("tooltip.firstmod.seed.tier").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(tier)).withStyle(ChatFormatting.GREEN)));

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

        ItemStack seedStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();

        float currentInfusePercent = seedStack.getOrDefault(INFUSE_PERCENT, 0.0f);
        int seedTier = seedStack.getOrDefault(TIER, 1);

        // Ensure the clicked block is the specific infusion block
        if(state.getBlock() != ModBlocks.INFUSER.get()) {
            return InteractionResult.FAIL;
        }

        if (!offHandStack.isEmpty() && offHandStack.is(INFUSABLES_TAG)) {
            if (currentInfusePercent >= 100.0f) {
                player.displayClientMessage(Component.translatable("message.firstmod.infusion_complete").withStyle(ChatFormatting.GREEN), true);
                return InteractionResult.FAIL;
            }

            Item infusedItem = offHandStack.getItem();
            String itemID = BuiltInRegistries.ITEM.getKey(infusedItem).toString();

            int itemTier = InfusionItemsConfig.getItemTier(itemID);

            int availableItems = offHandStack.getCount();

            if(itemTier == -1) {
                return InteractionResult.FAIL;
            }

            if(itemTier > seedTier) {
                player.displayClientMessage(Component.translatable("message.firstmod.high_tier",
                                infusedItem.getDescription())
                        .withStyle(ChatFormatting.YELLOW), true);

                return InteractionResult.FAIL;
            }

            // If it's a new infusion, set the item type
            if (currentInfusePercent == 0.0f) {
                seedStack.set(ITEM_TYPE, infusedItem);
            } else {
                Item previousItem = seedStack.get(ITEM_TYPE);
                // Ensure the infusion continues with the same item type
                if (previousItem != infusedItem) {
                    assert previousItem != null;
                    player.displayClientMessage(Component.translatable("message.firstmod.wrong_item", previousItem.getDescription()).withStyle(ChatFormatting.YELLOW), true);
                    return InteractionResult.FAIL;
                }
            }

            int tierInfusionAmount = Config.getInfusionAmountForTier(itemTier);
            double baseInfusePercent = Config.baseInfusion;
            float newInfusePercent;
            int itemsUsed = 0;
            int tierInfusePercent = (int) Math.ceil(tierInfusionAmount * baseInfusePercent);

            if (player.isShiftKeyDown()) {
                float neededIncrease = 100.0f - currentInfusePercent;
                itemsUsed = (int) Math.ceil(neededIncrease / tierInfusePercent);
                if (itemsUsed > availableItems) {
                    itemsUsed = availableItems;
                }
                newInfusePercent = Math.min(currentInfusePercent + (itemsUsed * tierInfusePercent), 100.0f);
            } else {
                newInfusePercent = Math.min(currentInfusePercent + tierInfusePercent, 100.0f);
                itemsUsed = 1;
            }

            seedStack.set(INFUSE_PERCENT, newInfusePercent);

            if (currentInfusePercent < 100.0f) {
                offHandStack.shrink(itemsUsed);
            } else {
                player.displayClientMessage(Component.translatable("message.firstmod.infusion_complete").withStyle(ChatFormatting.GREEN), true);
            }

            return InteractionResult.SUCCESS;
        } else if (!offHandStack.isEmpty() && offHandStack.getItem() instanceof SeedItem) {
            float offHandInfusePercent = offHandStack.getOrDefault(INFUSE_PERCENT, 0.0f);

            if (offHandInfusePercent > 0.0f && !player.isShiftKeyDown()) {
                player.displayClientMessage(Component.translatable("message.firstmod.shift_to_consume")
                        .withStyle(ChatFormatting.YELLOW), true);
                return InteractionResult.FAIL;
            }

            int offHandTier = offHandStack.getOrDefault(TIER, 1);

            if (seedTier == Config.tierCount) {
                player.displayClientMessage(Component.translatable("message.firstmod.max_tier")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }

            int newTier = Math.min(Math.max(seedTier, offHandTier) + 1, 4);

            player.sendSystemMessage(Component.literal("Upgrading Seed: Old Tier = " + seedTier + ", New Tier = " + newTier)
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            seedStack.set(TIER, newTier);

            player.getInventory().setItem(player.getInventory().selected, seedStack.copy());

            player.sendSystemMessage(Component.literal("SeedItem Updated: New Tier = " + seedStack.getOrDefault(TIER, 1))
                    .withStyle(ChatFormatting.GREEN));

            seedStack.set(INFUSE_PERCENT, seedStack.getOrDefault(INFUSE_PERCENT, 0.0f));
            seedStack.set(ITEM_TYPE, seedStack.getOrDefault(ITEM_TYPE, this));

            offHandStack.shrink(1);

            player.displayClientMessage(Component.translatable("message.firstmod.tier_upgraded",
                    newTier).withStyle(ChatFormatting.GREEN), true);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


}
