package fi.dy.masa.malilib.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.serialization.DataResult;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.*;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.DyeColor;
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

        // Standard Weapons / Armor types
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
        // BlockEntityTag, pre 24w09a
        if (nbt.contains("BlockEntityTag"))
        {
            NbtCompound beTag = nbt.getCompound("BlockEntityTag");
            // Handle
            compResult.add(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(beTag));
            nbt.remove("BlockEntityTag");
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
                    ComponentMap.Builder itemResult = ComponentMap.builder();
                    byte itemCount = itemNbt.getByte("Count");

                    if (itemNbt.contains("tag"))
                    {
                        NbtCompound itemTag = itemNbt.getCompound("tag").copy();
                        ProfileComponent skullProfile = null;

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

                        if (skullProfile != null)
                        {
                            itemResult.add(DataComponentTypes.PROFILE, skullProfile);
                        }
                        itemNbt.remove("tag");
                    }
                    if (itemNbt.contains("id"))
                    {
                        String itemId = itemNbt.getString("id");
                        ItemStack stack;

                        if (itemCount > 0)
                        {
                            stack = InventoryUtils.getItemStackFromString(itemId, itemCount);
                        }
                        else
                        {
                            stack = InventoryUtils.getItemStackFromString(itemId);
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
                                        NbtElement compElement = compNbt.get(key);

                                        if (compType != null)
                                        {
                                            try
                                            {
                                                DataResult<?> dataResult = Objects.requireNonNull(compType.getCodec()).parse(NbtOps.INSTANCE, compElement);
                                                //itemResult.add(compType, Optional.of(dataResult));
                                                // Doesn't work?  So how do we load Components from an NbtCompound ?
                                            }
                                            catch (Exception ignored)
                                            {
                                                MaLiLib.logger.error("fromBlockEntityNBT(): error parsing component entry for key {} of type {} with element {}", key, compType.toString(), compElement);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        stack.applyComponentsFrom(itemResult.build());
                        itemInv.add(stack);
                    }
                    else
                    {
                        MaLiLib.logger.error("fromBlockEntityNBT() received invalid Inventory data, \"id\" is missing for slot: {}", itemSlot);
                    }
                }
            }

        }
        // BannerItems, pre 24w09a
        if (nbt.contains("Patterns"))
        {
            NbtList patternList = nbt.getList("Patterns", 10);

            for (int i = 0; i < patternList.size() && i < 6; i++)
            {
                NbtCompound patternComp = patternList.getCompound(i);
                DyeColor dyeColor = DyeColor.byId(patternComp.getInt("Color"));
                String bannerPattern = patternComp.getString("Pattern");

                RegistryEntry<BannerPattern> patternReg;
                //Optional<RegistryEntry.Reference<BannerPattern>> optional = RegistryEntryLookup<BannerPattern>.getOptional(pattern);
            }
        }
        // BeeHiveBlock, pre 24w09a
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
        // BeeHiveBlock, post 24w10a
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
        // BeeHiveBlock entity data, pre 24w09a
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
        // BeeHiveBlock Entity Data, post 24w10a
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
