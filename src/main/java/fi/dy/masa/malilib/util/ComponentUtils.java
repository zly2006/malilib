package fi.dy.masa.malilib.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import fi.dy.masa.malilib.MaLiLib;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This file is meant to be a new central place for managing Component <-> NBT data
 * *** A WORK IN PROGRESS ***
 * -
 * These tend to be annoying, because for 24w09a and below, the NBT was the same.
 * Now, for 24w10a (And beyond) they are all stored IN LOWER CASE!
 * (i.e. "Slot" becomes "slot") So, in order to maintain this for backwards compatibility;
 * *all* of the possible NBT values need to be managed.
 */
public class ComponentUtils
{
    public static final ComponentMap EMPTY = ComponentMap.EMPTY;
    public static final Pattern PATTERN_COMPONENT_BASE = Pattern.compile("^(?<name>(?:[a-z0-9\\._-]+:)[a-z0-9\\._-]+)$");
    public ComponentMap fromItemNBT(NbtCompound nbt)
    {
        ComponentMap.Builder compResult = ComponentMap.builder();

        if (nbt.contains("AttributeModifiers", 9))
        {
            AttributeModifiersComponent.Builder attribs = AttributeModifiersComponent.builder();
            NbtList attribList = nbt.getList("AttributeModifiers", 10);

            for (int i = 0; i < attribList.size(); i++)
            {
                NbtCompound attribNbt = attribList.getCompound(i);
                String attribSlot = "";
                String attribName;

                if (attribNbt.contains("Slot"))
                {
                    attribSlot = attribNbt.getString("Slot");
                }
                else if (attribNbt.contains("slot"))
                {
                    attribSlot = attribNbt.getString("slot");
                }
                attribName = attribNbt.getString("AttributeName");

                if (!attribName.isEmpty() && !attribSlot.isEmpty())
                {
                    Optional<EntityAttribute> attribValue = Registries.ATTRIBUTE.getOrEmpty(Identifier.tryParse(attribName));

                    if (attribValue.isPresent())
                    {
                        EntityAttributeModifier entAttribMod = EntityAttributeModifier.fromNbt(attribNbt);

                        if (entAttribMod != null)
                        {
                            AttributeModifierSlot attribModSlot = getAttribModSlot(attribSlot);
                            RegistryEntry<EntityAttribute> newAttribValue = RegistryEntry.of(attribValue.get());

                            attribs.add(newAttribValue, entAttribMod, attribModSlot);
                        }
                    }
                }
            }

            nbt.remove("AttributeModifiers");
            AttributeModifiersComponent attribResult = attribs.build();
            compResult.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attribResult);
        }

        return compResult.build();
    }

    public ComponentMap fromBlockEntityNBT(NbtCompound nbt)
    {
        BlockPos blockPos;
        Identifier blockId;

        if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z"))
        {
            blockPos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        }
        if (nbt.contains("id"))
        {
            String entityType;
            entityType = nbt.getString("id");
            blockId = Identifier.tryParse(entityType);
        }
        else
        {
            // Invalid NBT data
            MaLiLib.logger.error("fromBlockEntityNBT() received invalid NBT data, \"id\" is missing!");
            return null;
        }

        ComponentMap.Builder compResult = ComponentMap.builder();

        if (nbt.contains("Items"))
        {
            NbtList itemList = nbt.getList("Items", 10);
            DefaultedList<ItemStack> itemInv = DefaultedList.ofSize(itemList.size(), ItemStack.EMPTY);

            for (int i = 0; i < itemList.size(); i++)
            {
                NbtCompound itemNbt = itemList.getCompound(i);
                int itemSlot = itemNbt.getByte("Slot") & 255;
                if (itemSlot >= 0 && itemSlot < itemInv.size())
                {
                    byte itemCount = itemNbt.getByte("Count");
                    if (itemNbt.contains("tag"))
                    {
                        NbtCompound itemTag = itemNbt.getCompound("tag").copy();
                        ProfileComponent skullProfile;

                        // The only "Item" class that uses this is PlayerHeadItem.
                        if (itemTag.contains("SkullOwner", 10))
                        {
                            // Has GameProfile
                            NbtCompound skullOwner = itemTag.getCompound("SkullOwner");
                            skullProfile = getSkullProfileFromNBT(skullOwner);
                        }
                        else if (itemTag.contains("SkullOwner", 8))
                        {
                            // Doesn't have GameProfile
                            String skullString = itemTag.getString("SkullOwner");
                            skullProfile = getSkullProfileFromString(skullString);
                        }
                        else if (itemTag.contains("ExtraType", 8))
                        {
                            String skullUUID = itemTag.getString("ExtraType");
                            skullProfile = getSkullProfileFromString(skullUUID);
                        }
                        else if (itemTag.contains("profile"))
                        {
                            // Post 24w09a Component Profile
                            NbtCompound skullOwner = itemTag.getCompound("profile");
                            skullProfile = getSkullProfileFromProfile(skullOwner);
                        }

                        itemNbt.remove("tag");
                    }
                    if (itemNbt.contains("id"))
                    {
                        String itemId = itemNbt.getString("id");

                        if (itemCount > 0)
                        {
                            itemInv.set(itemSlot, InventoryUtils.getItemStackFromString(itemId, itemCount));
                        }
                        else
                        {
                            itemInv.set(itemSlot, InventoryUtils.getItemStackFromString(itemId));
                        }
                        if (itemNbt.contains("components"))
                        {
                            NbtCompound compNbt = itemNbt.getCompound("components");

                            for (String key : compNbt.getKeys())
                            {
                                Matcher matcher = PATTERN_COMPONENT_BASE.matcher(key);

                                if (matcher.matches())
                                {
                                    String compName = matcher.group("name");

                                    if (compName != null)
                                    {
                                        Identifier compId = new Identifier(compName);
                                        DataComponentType<?> compType = Registries.DATA_COMPONENT_TYPE.get(compId);
                                        //NbtComponent nbtComponent = NbtComponent.of(compNbt.getCompound(key));
                                        // How to convert the Component from the type?
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        MaLiLib.logger.error("fromBlockEntityNBT() received invalid Inventory data, \"id\" is missing for slot: {}", itemSlot);
                    }
                }
            }
        }
        // 24w09a- BeeHiveBlock Bees format
        if (nbt.contains("Bees"))
        {
            NbtList beeNbtList = nbt.getList("Bees", 10);
            List<BeehiveBlockEntity.BeeData> beeList = new ArrayList<>();

            for (int i = 0; i < beeNbtList.size(); i++)
            {
                NbtCompound beeNbt = beeNbtList.getCompound(i);
                BeehiveBlockEntity.BeeData beeData;

                if (beeNbt.contains("EntityData"))
                {
                    NbtCompound beeEnt = beeNbt.getCompound("EntityData").copy();
                    int beeTicksInHive = beeNbt.getInt("TicksInHive");
                    int occupationTicks = beeNbt.getInt("MinOccupationTicks");

                    beeData = new BeehiveBlockEntity.BeeData(NbtComponent.of(beeEnt), beeTicksInHive, occupationTicks);
                    beeList.add(beeData);
                }
            }

            nbt.remove("Bees");
            compResult.add(DataComponentTypes.BEES, beeList);
        }
        // 24w10a+ BeeHiveBlock Bees format
        else if (nbt.contains("bees"))
        {
            List<BeehiveBlockEntity.BeeData> beeList = new ArrayList<>();

            /*
            for (int i = 0; i < beeNbtList.getSize(); i++)
            {
                NbtCompound beeNbt = beeNbtList.getCompound(i);
                BeehiveBlockEntity.BeeData beeData;

                if (beeNbt.contains("entity_data"))
                {
                    NbtCompound beeEnt2 = beeNbt.getCompound("entity_data");
                    String beeId = beeEnt2.getString("id");
                    // beeId = Registries.ENTITY_TYPE.getId(EntityType.BEE).toString();
                    int beeTicksInHive2 = beeNbt.getInt("ticks_in_hive");
                    int occupationTicks2 = beeNbt.getInt("min_ticks_in_hive");

                    beeData = new BeehiveBlockEntity.BeeData(NbtComponent.of(beeEnt2), beeTicksInHive2, occupationTicks2);
                    beeList.add(beeData);
                }
            }
             */
            BeehiveBlockEntity.BeeData.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get("bees")).resultOrPartial((result) ->
            {
                // ignore
            }).ifPresent(beeList::addAll);
            if (!beeList.isEmpty())
            {
                compResult.add(DataComponentTypes.BEES, beeList);
            }
            nbt.remove("bees");
        }
        // 24w09a- BeeHiveBlock entity data
        if (nbt.contains("FlowerPos"))
        {
            // The FlowerPos isn't used in the ComponentData, but it is part of the Beehive Block Entity Data.
            int[] flowerArray;
            BlockPos flowerPos;

            flowerArray = nbt.getIntArray("FlowerPos");
            if (flowerArray.length == 3)
            {
                flowerPos = new BlockPos(flowerArray[0], flowerArray[1], flowerArray[2]);
            }
        }
        // 24w10a+ BeeHiveBlock Entity Data
        if (nbt.contains("flower_pos"))
        {
            // The FlowerPos isn't used in the ComponentData, but it is part of the Beehive Block Entity Data.
            int[] flowerArray;
            BlockPos flowerPos;

            flowerArray = nbt.getIntArray("flower_pos");
            if (flowerArray.length == 3)
            {
                flowerPos = new BlockPos(flowerArray[0], flowerArray[1], flowerArray[2]);
            }
        }

        return compResult.build();
    }

    @Nullable
    private ProfileComponent getSkullProfileFromProfile(NbtCompound profile)
    {
        UUID uuid = Util.NIL_UUID;
        String name = "";
        NbtCompound properties;
        GameProfile skullProfile;

        if (profile.contains("id"))
        {
            uuid = profile.getUuid("id");
        }
        if (profile.contains("name"))
        {
            name = profile.getString("name");
        }
        if (!name.isEmpty())
        {
            try
            {
                skullProfile = new GameProfile(uuid, name);
                if (profile.contains("properties"))
                {
                    properties = profile.getCompound("properties");

                    for (String key : properties.getKeys())
                    {
                        NbtList propList = properties.getList(key, 10);

                        for (int i = 0; i < propList.size(); i++)
                        {
                            NbtCompound propNbt = propList.getCompound(i);

                            String value = propNbt.getString("value");
                            String propName = propNbt.getString("name");

                            PropertyMap newMap;

                            if (propNbt.contains("signature", 8))
                            {
                                skullProfile.getProperties().put(key, new Property(propName, value, propNbt.getString("signature")));
                            }
                            else
                            {
                                skullProfile.getProperties().put(key, new Property(propName, value));
                            }

                        }
                    }
                }

                return new ProfileComponent(skullProfile);
            }
            catch (Exception failure)
            {
                MaLiLib.logger.error("getSkullProfileFromProfile() failed to retrieve GameProfile from post-24w09a type data");
            }
        }
        else
        {
            MaLiLib.logger.error("getSkullProfileFromProfile() failed to retrieve GameProfile from post-24w09a type data (name or id is empty)");
        }

        return null;
    }

    @Nullable
    private ProfileComponent getSkullProfileFromNBT(NbtCompound skullOwner)
    {
        UUID uuid = Util.NIL_UUID;
        String name = "";
        NbtCompound properties;
        GameProfile skullProfile;

        // 24w09- "SkullOwner" Format
        if (skullOwner.contains("Id"))
        {
            uuid = skullOwner.getUuid("Id");
        }
        if (skullOwner.contains("Name"))
        {
            name = skullOwner.getString("Name");
        }
        if (!name.isEmpty())
        {
            try
            {
                skullProfile = new GameProfile(uuid, name);
                if (skullOwner.contains("Properties"))
                {
                    properties = skullOwner.getCompound("Properties");

                    for (String key : properties.getKeys())
                    {
                        NbtList propList = properties.getList(key, 10);

                        for (int i = 0; i < propList.size(); i++)
                        {
                            NbtCompound propNbt = propList.getCompound(i);

                            String value = propNbt.getString("Value");
                            if (propNbt.contains("Signature", 8))
                            {
                                skullProfile.getProperties().put(key, new Property(key, value, propNbt.getString("Signature")));
                            }
                            else
                            {
                                skullProfile.getProperties().put(key, new Property(key, value));
                            }
                        }
                    }
                }

                return new ProfileComponent(skullProfile);
            }
            catch (Exception failure)
            {
                MaLiLib.logger.error("getSkullProfileFromNBT() failed to retrieve GameProfile from pre-24w09a type data");
            }
        }
        else
        {
            MaLiLib.logger.error("getSkullProfileFromNBT() failed to retrieve GameProfile from pre-24w09a type data (name or id is empty)");
        }

        return null;
    }

    private ProfileComponent getSkullProfileFromString(String skullString)
    {
        GameProfile skullProfile = new GameProfile(Util.NIL_UUID, skullString);
        return new ProfileComponent(skullProfile);
    }

    /**
     * I would assume there is a better way to do this, but I couldn't find it
     */
    private AttributeModifierSlot getAttribModSlot(String attribSlot)
    {
        if (AttributeModifierSlot.ANY.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.ANY;
        }
        else if (AttributeModifierSlot.MAINHAND.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.MAINHAND;
        }
        else if (AttributeModifierSlot.OFFHAND.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.OFFHAND;
        }
        else if (AttributeModifierSlot.HAND.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.HAND;
        }
        else if (AttributeModifierSlot.FEET.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.FEET;
        }
        else if (AttributeModifierSlot.LEGS.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.LEGS;
        }
        else if (AttributeModifierSlot.CHEST.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.CHEST;
        }
        else if (AttributeModifierSlot.HEAD.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.HEAD;
        }
        else if (AttributeModifierSlot.ARMOR.asString().equals(attribSlot))
        {
            return AttributeModifierSlot.ARMOR;
        }
        else
        {
            return AttributeModifierSlot.ANY;
        }
    }
}
