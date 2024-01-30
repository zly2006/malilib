package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface ISyncmaticaPayloadListener
{
    default void sendSyncmaticaPayload(NbtCompound data) { }
    default void receiveSyncmaticaPayload(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    default void encodeSyncmaticaPayload(NbtCompound data, Identifier id) { }
    default void decodeSyncmaticaPayload(NbtCompound data, Identifier id) { }
}
