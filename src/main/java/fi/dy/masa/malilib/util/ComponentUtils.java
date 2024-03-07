package fi.dy.masa.malilib.util;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.custom.DebugBeeCustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This file is meant to be a new central place for managing Component <-> NBT data
 */
public class ComponentUtils
{
    public static final ComponentMap EMPTY = ComponentMap.EMPTY;

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
        if (nbt.contains("Bees") || nbt.contains("bees"))
        {
            NbtList beeNbtList;
            List<BeehiveBlockEntity.BeeData> beeList = new ArrayList<>();

            if (nbt.contains("Bees"))
            {
                beeNbtList = nbt.getList("Bees", 10);
            }
            else
            {
                beeNbtList = nbt.getList("bees", 10);
            }
        }

        return compResult.build();
    }

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
