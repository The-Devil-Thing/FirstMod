package net.thedevilthing.firstmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.thedevilthing.firstmod.Config;
import net.thedevilthing.firstmod.component.ModDataComponents;

import java.util.List;

public class SeedItem extends Item{

    public SeedItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    private static final DataComponentType<Float> INFUSE_PERCENT = ModDataComponents.INFUSE_PERCENT.get();
    private static final DataComponentType<Item> ITEM_TYPE = ModDataComponents.ITEM_TYPE.get();
    private static final DataComponentType<Integer> TIER = ModDataComponents.TIER.get();

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        Float infuse_percent = stack.getOrDefault(INFUSE_PERCENT, 0.0f);
        Item itemType = stack.getOrDefault(ITEM_TYPE, Items.AIR);
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

        int seedTier = seedStack.getOrDefault(TIER, 1);

        if (!offHandStack.isEmpty() && offHandStack.getItem() instanceof SeedItem) {
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

            player.displayClientMessage(Component.translatable("message.firstmod.tier_upgraded", newTier).withStyle(ChatFormatting.GREEN), true);

            return InteractionResult.SUCCESS;
        } else if (offHandStack.isEmpty() && state.getBlock() == Blocks.GRINDSTONE && player.isCrouching()) {
            seedStack.set(ITEM_TYPE, Items.AIR);
            seedStack.set(INFUSE_PERCENT, 0.0f);

            player.displayClientMessage(Component.translatable("message.firstmod.wipe_seed"), true);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


}
