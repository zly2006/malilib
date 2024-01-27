package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IServuxPayloadListener
{
    default void reset() { }
    default void receiveServuxPayload(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    default void sendServuxPayload(NbtCompound data) { }
    default void encodeServuxPayload(NbtCompound data, Identifier id) { }
    default void decodeServuxPayload(NbtCompound data, Identifier id) { }
}
