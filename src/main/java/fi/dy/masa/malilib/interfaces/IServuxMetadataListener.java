package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IServuxMetadataListener
{
    default void reset() { }
    default void receiveServuxMetadata(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    default void sendServuxMetadata(NbtCompound data) { }
    default void encodeServuxMetadata(NbtCompound data, Identifier id) { }
    default void decodeServuxMetadata(NbtCompound data, Identifier id) { }
}
