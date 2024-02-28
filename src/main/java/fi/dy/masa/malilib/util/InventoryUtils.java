package fi.dy.masa.malilib.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryUtils
{
    //public static final ImmutableSet<String> DAMAGE_KEY = ImmutableSet.of("Damage");
    public static final Set<class_9331<?>> DAMAGE_KEY = Set.of(class_9334.DAMAGE);
    private static final DefaultedList<ItemStack> EMPTY_LIST = DefaultedList.of();

    /**
     * @return true if the stacks are identical otherwise, but ignoring the stack size
     */
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2)
    {
        //return ItemStack.canCombine(stack1, stack2);
        return ItemStack.areItemsAndNbtEqual(stack1, stack2);
    }

    /**
     * @return true if the stacks are identical otherwise, but ignoring the stack size,
     * and if the item is damageable, then ignoring the damage too.
     */
    public static boolean areStacksEqualIgnoreDurability(ItemStack stack1, ItemStack stack2)
    {
        if (!ItemStack.areItemsEqual(stack1, stack2))
        {
            return false;
        }

        //NbtCompound tag1 = stack1.getNbt();
        //NbtCompound tag2 = stack2.getNbt();

        // FIXME --> class_9323 == DataComponentMap class via Mojang Mappings
        class_9323 tag1 = stack1.method_57353();
        class_9323 tag2 = stack2.method_57353();

        if (tag1 == null || tag2 == null)
        {
            return tag1 == tag2;
        }

        if (!stack1.isDamageable() && !stack2.isDamageable())
        {
            return Objects.equals(tag1, tag2);
        }

        return areNbtEqualIgnoreKeys(tag1, tag2, DAMAGE_KEY);
        //return areNbtEqualIgnoreKeys(tag1, tag2, Set.of(class_9334.DAMAGE));
    }

    public static boolean areNbtEqualIgnoreKeys(class_9323 tag1, class_9323 tag2, Set<class_9331<?>> ignoredKeys)
    {
        //Set<String> keys1 = tag1.getKeys();
        //Set<String> keys2 = tag2.getKeys();

        // FIXME class_9331 == DataComponentType via Mojang Mappings
        Set<class_9331<?>> keys1 = tag1.method_57831();
        Set<class_9331<?>> keys2 = tag2.method_57831();

        keys1.removeAll(ignoredKeys);
        keys2.removeAll(ignoredKeys);

        if (!Objects.equals(keys1, keys2))
        {
            return false;
        }

        for (class_9331<?> key : keys1)
        {
            // FIXME method_57829 == get()
            if (!Objects.equals(tag1.method_57829(key), tag2.method_57829(key)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Swaps the stack from the slot <b>slotNum</b> to the given hotbar slot <b>hotbarSlot</b>
     * @param container
     * @param slotNum
     * @param hotbarSlot
     */
    public static void swapSlots(ScreenHandler container, int slotNum, int hotbarSlot)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        assert mc.interactionManager != null;
        mc.interactionManager.clickSlot(container.syncId, slotNum, hotbarSlot, SlotActionType.SWAP, mc.player);
    }

    /**
     * Assuming that the slot is from the ContainerPlayer container,
     * returns whether the given slot number is one of the regular inventory slots.
     * This means that the crafting slots and armor slots are not valid.
     * @param slotNumber
     * @param allowOffhand
     * @return
     */
    public static boolean isRegularInventorySlot(int slotNumber, boolean allowOffhand)
    {
        return slotNumber > 8 && (allowOffhand || slotNumber < 45);
    }

    /**
     * Finds an empty slot in the player inventory. Armor slots are not valid for the return value of this method.
     * Whether or not the offhand slot is valid, depends on the <b>allowOffhand</b> argument.
     * @param containerPlayer
     * @param allowOffhand
     * @param reverse
     * @return the slot number, or -1 if none were found
     */
    public static int findEmptySlotInPlayerInventory(ScreenHandler containerPlayer, boolean allowOffhand, boolean reverse)
    {
        final int startSlot = reverse ? containerPlayer.slots.size() - 1 : 0;
        final int endSlot = reverse ? -1 : containerPlayer.slots.size();
        final int increment = reverse ? -1 : 1;

        for (int slotNum = startSlot; slotNum != endSlot; slotNum += increment)
        {
            Slot slot = containerPlayer.slots.get(slotNum);
            ItemStack stackSlot = slot.getStack();

            // Inventory crafting, armor and offhand slots are not valid
            if (stackSlot.isEmpty() && isRegularInventorySlot(slot.id, allowOffhand))
            {
                return slot.id;
            }
        }

        return -1;
    }

    /**
     * Finds a slot with an identical item than <b>stackReference</b>, ignoring the durability
     * of damageable items. Does not allow crafting or armor slots or the offhand slot
     * in the ContainerPlayer container.
     * @param container
     * @param stackReference
     * @param reverse
     * @return the slot number, or -1 if none were found
     */
    public static int findSlotWithItem(ScreenHandler container, ItemStack stackReference, boolean reverse)
    {
        final int startSlot = reverse ? container.slots.size() - 1 : 0;
        final int endSlot = reverse ? -1 : container.slots.size();
        final int increment = reverse ? -1 : 1;
        final boolean isPlayerInv = container instanceof PlayerScreenHandler;

        for (int slotNum = startSlot; slotNum != endSlot; slotNum += increment)
        {
            Slot slot = container.slots.get(slotNum);

            if ((!isPlayerInv || isRegularInventorySlot(slot.id, false)) &&
                areStacksEqualIgnoreDurability(slot.getStack(), stackReference))
            {
                return slot.id;
            }
        }

        return -1;
    }

    /**
     * Swap the given item to the player's main hand, if that item is found
     * in the player's inventory.
     * @param stackReference
     * @param mc
     * @return true if an item was swapped to the main hand, false if it was already in the hand, or was not found in the inventory
     */
    public static boolean swapItemToMainHand(ItemStack stackReference, MinecraftClient mc)
    {
        PlayerEntity player = mc.player;
        assert player != null;
        boolean isCreative = player.isCreative();

        // Already holding the requested item
        if (areStacksEqual(stackReference, player.getMainHandStack()))
        {
            return false;
        }

        if (isCreative)
        {
            player.getInventory().addPickBlock(stackReference);
            assert mc.interactionManager != null;
            mc.interactionManager.clickCreativeStack(player.getMainHandStack(), 36 + player.getInventory().selectedSlot); // sendSlotPacket
            return true;
        }
        else
        {
            int slot = findSlotWithItem(player.playerScreenHandler, stackReference, true);

            if (slot != -1)
            {
                int currentHotbarSlot = player.getInventory().selectedSlot;
                assert mc.interactionManager != null;
                mc.interactionManager.clickSlot(player.playerScreenHandler.syncId, slot, currentHotbarSlot, SlotActionType.SWAP, mc.player);
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the inventory at the given position, if any.
     * Combines chest inventories into double chest inventories when applicable.
     * @param world
     * @param pos
     * @return
     */
    @Nullable
    public static Inventory getInventory(World world, BlockPos pos)
    {
        @SuppressWarnings("deprecation")
        boolean isLoaded = world.isChunkLoaded(pos);

        if (!isLoaded)
        {
            return null;
        }

        // The method in World now checks that the caller is from the same thread...
        BlockEntity te = world.getWorldChunk(pos).getBlockEntity(pos);

        if (te instanceof Inventory inv)
        {
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof ChestBlock && te instanceof ChestBlockEntity)
            {
                ChestType type = state.get(ChestBlock.CHEST_TYPE);

                if (type != ChestType.SINGLE)
                {
                    BlockPos posAdj = pos.offset(ChestBlock.getFacing(state));
                    @SuppressWarnings("deprecation")
                    boolean isLoadedAdj = world.isChunkLoaded(posAdj);

                    if (isLoadedAdj)
                    {
                        BlockState stateAdj = world.getBlockState(posAdj);
                        // The method in World now checks that the caller is from the same thread...
                        BlockEntity te2 = world.getWorldChunk(posAdj).getBlockEntity(posAdj);

                        if (stateAdj.getBlock() == state.getBlock() &&
                            te2 instanceof ChestBlockEntity &&
                            stateAdj.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE &&
                            stateAdj.get(ChestBlock.FACING) == state.get(ChestBlock.FACING))
                        {
                            Inventory invRight = type == ChestType.RIGHT ?             inv : (Inventory) te2;
                            Inventory invLeft  = type == ChestType.RIGHT ? (Inventory) te2 :             inv;
                            inv = new DoubleInventory(invRight, invLeft);
                        }
                    }
                }
            }

            return inv;
        }

        return null;
    }

    /**
     * Checks if the given Shulker Box (or other storage item with the
     * same NBT data structure) currently contains any items.
     * @param stackShulkerBox
     * @return
     */
    public static boolean shulkerBoxHasItems(ItemStack stackShulkerBox)
    {
        //NbtCompound nbt = stackShulkerBox.getNbt();

        // FIXME --> class_9323 == DataComponentMap class via Mojang Mappings
        class_9323 data = stackShulkerBox.method_57353();

        //data.method_57832(class_9334.BLOCK_ENTITY_DATA);

        //if (nbt != null && nbt.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND))
        if (data != null && data.method_57832(class_9334.BLOCK_ENTITY_DATA))
        {
            //NbtCompound tag = nbt.getCompound("BlockEntityTag");

            // FIXME class_9279 == CustomData class via Mojang Mappings
            class_9279 customTag = data.method_57829(class_9334.BLOCK_ENTITY_DATA);

            // FIXME method_57458() is .isEmpty() call via Mojang Mappings
            if (!customTag.method_57458())
            {
                NbtCompound tag = customTag.method_57461();

                if (tag.contains("Items", Constants.NBT.TAG_LIST))
                {
                    NbtList tagList = tag.getList("Items", Constants.NBT.TAG_COMPOUND);
                    return tagList.size() > 0;
                }
            }
            else
                return false;
        }

        return false;
    }

    /**
     * Returns the list of items currently stored in the given Shulker Box
     * (or other storage item with the same NBT data structure).
     * Does not keep empty slots.
     * @param stackIn The item holding the inventory contents
     * @return
     */
    public static DefaultedList<ItemStack> getStoredItems(ItemStack stackIn)
    {
        class_9288 itemContents = stackIn.method_57379(class_9334.CONTAINER, class_9288.field_49334);

        if (itemContents != null)
        {
            DefaultedList<ItemStack> items = DefaultedList.of();

            itemContents.method_57492(items);

            return items;
        }
        else
            return EMPTY_LIST;
    }

        /*
        //NbtCompound nbt = stackIn.getNbt();

        // FIXME --> class_9323 == DataComponentMap class via Mojang Mappings
        class_9323 data = stackIn.method_57353();

        //if (nbt != null && nbt.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND))

        if (data != null && data.method_57832(class_9334.BLOCK_ENTITY_DATA))
        {
            //NbtCompound tagBlockEntity = nbt.getCompound("BlockEntityTag");

            // FIXME class_9279 == CustomData class via Mojang Mappings
            class_9279 customTag = data.method_57829(class_9334.BLOCK_ENTITY_DATA);

            // FIXME method_57458() is .isEmpty() call via Mojang Mappings
            if (!customTag.method_57458())
            {
                NbtCompound tagBlockEntity = customTag.method_57461();

                if (tagBlockEntity.contains("Items", Constants.NBT.TAG_LIST))
                {
                    DefaultedList<ItemStack> items = DefaultedList.of();
                    NbtList tagList = tagBlockEntity.getList("Items", Constants.NBT.TAG_COMPOUND);
                    final int count = tagList.size();

                    for (int i = 0; i < count; ++i)
                    {
                        //ItemStack stack = ItemStack.fromNbt(tagList.getCompound(i));

                        //customTag
                        tagList.getCompound(i);
                        ItemStack stack = ItemStack.method_57353();

                        if (!stack.isEmpty())
                        {
                            items.add(stack);
                        }
                    }

                    return items;
                }
            }
            else
                return DefaultedList.of();
        }

        return DefaultedList.of();
                 */

    /**
     * Returns the list of items currently stored in the given Shulker Box
     * (or other storage item with the same NBT data structure).
     * Preserves empty slots.
     * @param stackIn The item holding the inventory contents
     * @param slotCount the maximum number of slots, and thus also the size of the list to create
     * @return
     */
    public static DefaultedList<ItemStack> getStoredItems(ItemStack stackIn, int slotCount)
    {
        class_9288 itemContents = stackIn.method_57379(class_9334.CONTAINER, class_9288.field_49334);

        if (itemContents != null)
        {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(slotCount, ItemStack.EMPTY);

            //itemContents.method_57492(items);

            Iterator<ItemStack> iter = itemContents.iterator();

            for (int i = 0; i < slotCount; i++)
            {
                if (iter.hasNext())
                {
                    items.add(iter.next());
                }
            }

            return items;
        }
        else
            return EMPTY_LIST;
    }

        /*
        NbtCompound nbt = stackIn.getNbt();

        if (nbt != null && nbt.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND))
        {
            NbtCompound tagBlockEntity = nbt.getCompound("BlockEntityTag");

            if (tagBlockEntity.contains("Items", Constants.NBT.TAG_LIST))
            {
                NbtList tagList = tagBlockEntity.getList("Items", Constants.NBT.TAG_COMPOUND);
                final int count = tagList.size();
                int maxSlot = -1;

                if (slotCount <= 0)
                {
                    for (int i = 0; i < count; ++i)
                    {
                        NbtCompound tag = tagList.getCompound(i);
                        int slot = tag.getByte("Slot");

                        if (slot > maxSlot)
                        {
                            maxSlot = slot;
                        }
                    }

                    slotCount = maxSlot + 1;
                }

                DefaultedList<ItemStack> items = DefaultedList.ofSize(slotCount, ItemStack.EMPTY);

                for (int i = 0; i < count; ++i)
                {
                    NbtCompound tag = tagList.getCompound(i);
                    ItemStack stack = ItemStack.fromNbt(tag);
                    int slot = tag.getByte("Slot");

                    if (slot >= 0 && slot < items.size() && !stack.isEmpty())
                    {
                        items.set(slot, stack);
                    }
                }

                return items;
            }
        }

        return EMPTY_LIST;

         */

    /**
     * Returns a map of the stored item counts in the given Shulker Box
     * (or other storage item with the same NBT data structure).
     * @param stackShulkerBox
     * @return
     */
    public static Object2IntOpenHashMap<ItemType> getStoredItemCounts(ItemStack stackShulkerBox)
    {
        Object2IntOpenHashMap<ItemType> map = new Object2IntOpenHashMap<>();
        DefaultedList<ItemStack> items = getStoredItems(stackShulkerBox);

        for (ItemStack stack : items)
        {
            if (!stack.isEmpty())
            {
                map.addTo(new ItemType(stack), stack.getCount());
            }
        }

        return map;
    }

    /**
     * Returns a map of the stored item counts in the given inventory.
     * This also counts the contents of any Shulker Boxes
     * (or other storage item with the same NBT data structure).
     * @param inv
     * @return
     */
    public static Object2IntOpenHashMap<ItemType> getInventoryItemCounts(Inventory inv)
    {
        Object2IntOpenHashMap<ItemType> map = new Object2IntOpenHashMap<>();
        final int slots = inv.size();

        for (int slot = 0; slot < slots; ++slot)
        {
            ItemStack stack = inv.getStack(slot);

            if (!stack.isEmpty())
            {
                map.addTo(new ItemType(stack, false, true), stack.getCount());

                if (stack.getItem() instanceof BlockItem &&
                    ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock &&
                    shulkerBoxHasItems(stack))
                {
                    Object2IntOpenHashMap<ItemType> boxCounts = getStoredItemCounts(stack);

                    for (ItemType type : boxCounts.keySet())
                    {
                        map.addTo(type, boxCounts.getInt(type));
                    }
                }
            }
        }

        return map;
    }

    /**
     * Returns the given list of items wrapped as an InventoryBasic
     * @param items
     * @return
     */
    public static Inventory getAsInventory(DefaultedList<ItemStack> items)
    {
        SimpleInventory inv = new SimpleInventory(items.size());

        for (int slot = 0; slot < items.size(); ++slot)
        {
            inv.setStack(slot, items.get(slot));
        }

        return inv;
    }
}
