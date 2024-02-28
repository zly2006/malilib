package fi.dy.masa.malilib.util;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * A wrapper around ItemStack, that implements hashCode() and equals().
 * Whether or not the NBT data is considered by those methods,
 * depends on the checkNBT argument to the constructor.
 */
public class ItemType
{
    private ItemStack stack;
    private final boolean checkNBT;

    public ItemType(ItemStack stack)
    {
        this(stack, true, true);
    }

    public ItemType(ItemStack stack, boolean copy, boolean checkNBT)
    {
        this.stack = stack.isEmpty() ? ItemStack.EMPTY : (copy ? stack.copy() : stack);
        this.checkNBT = checkNBT;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public boolean checkNBT()
    {
        return this.checkNBT;
    }

    public void setStack(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.stack.getItem().hashCode();

        if (this.checkNBT())
        {
            // FIXME --> New method "DataComponents" replaces NbtCompound
            //  --> Loom needs to rename these yet, method_57353() == getComponents() under Mojang Mappings
            result = prime * result + (this.stack.method_57353() != null ? this.stack.method_57353().hashCode() : 0);
        }

        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        ItemType other = (ItemType) obj;

        if (this.stack.isEmpty() || other.stack.isEmpty())
        {
            return this.stack.isEmpty() == other.stack.isEmpty();
        }
        else
        {
            if (this.stack.getItem() != other.stack.getItem())
            {
                return false;
            }

            // FIXME --> New method "DataComponents" replaces NbtCompound
            //  --> Loom needs to rename these yet, method_57353() == getComponents() under Mojang Mappings
            return !this.checkNBT() || Objects.equals(this.stack.method_57353(), other.stack.method_57353());
        }
    }

    @Override
    public String toString()
    {
        if (this.checkNBT())
        {
            // FIXME --> New method "DataComponents" replaces NbtCompound
            //  --> Loom needs to rename these yet, method_57353() == getComponents() under Mojang Mappings
            Identifier rl = Registries.ITEM.getId(this.stack.getItem());
            return rl + " " + this.stack.method_57353();
        }
        else
        {
            return Registries.ITEM.getId(this.stack.getItem()).toString();
        }
    }
}
