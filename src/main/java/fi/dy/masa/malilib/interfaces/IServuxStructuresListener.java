package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IServuxStructuresListener
{
    default void reset() { }
    default void receiveServuxStructures(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    default void sendServuxStructures(NbtCompound data) { }
    default void encodeServuxStructures(NbtCompound data, Identifier id) { }
    default void decodeServuxStructures(NbtCompound data, Identifier id) { }
}
