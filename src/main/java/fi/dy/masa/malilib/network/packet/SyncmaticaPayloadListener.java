package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.interfaces.ISyncmaticaPayloadListener;
import fi.dy.masa.malilib.network.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.SyncmaticaPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class SyncmaticaPayloadListener implements ISyncmaticaPayloadListener
{
    @Override
    public void sendSyncmaticaPayload(NbtCompound data)
    {
        MaLiLib.printDebug("SyncmaticaPayloadListener#sendSyncmaticaPayload(): sending payload of size: {}", data.getSizeInBytes());
        SyncmaticaPayload payload = new SyncmaticaPayload(data);
        ClientNetworkPlayHandler.sendSyncmatica(payload);
    }

    @Override
    public void receiveSyncmaticaPayload(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id)
    {
        decodeSyncmaticaPayload(data, id);
    }

    @Override
    public void encodeSyncmaticaPayload(NbtCompound data, Identifier id)
    {
        // Client->Server (C2S) encoder
        NbtCompound nbt = new NbtCompound();
        nbt.copyFrom(data);
        MaLiLib.printDebug("SyncmaticaPayloadListener#encodeSyncmaticaPayload(): encoding payload of size: {}", data.getSizeInBytes());
        sendSyncmaticaPayload(nbt);
    }

    @Override
    public void decodeSyncmaticaPayload(NbtCompound data, Identifier id)
    {
        // Server->Client (S2C) decoder
        MaLiLib.printDebug("SyncmaticaPayloadListener#decodeSyncmaticaPayload(): decoding payload of size: {}", data.getSizeInBytes());
        String hello = data.getString("hello");
        MaLiLib.printDebug("SyncmaticaPayloadListener#decodeSyncmaticaPayload(): id: {}, received: {}", id.toString(), hello);
    }
}
