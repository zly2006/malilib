package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IServuxLitematicsListener
{
    default void reset() { }
    default void receiveServuxLitematics(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    default void sendServuxLitematics(NbtCompound data) { }
    default void encodeServuxLitematics(NbtCompound data, Identifier id) { }
    default void decodeServuxLitematics(NbtCompound data, Identifier id) { }
}
