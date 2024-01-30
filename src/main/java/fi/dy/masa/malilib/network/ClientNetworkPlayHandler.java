package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.event.CarpetHandler;
import fi.dy.masa.malilib.event.ServuxPayloadHandler;
import fi.dy.masa.malilib.event.SyncmaticaPayloadHandler;
import fi.dy.masa.malilib.network.payload.*;
import fi.dy.masa.malilib.util.PayloadUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

public class ClientNetworkPlayHandler
{
    // String Payload
    public static void sendString(StringPayload payload)
    {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveString(StringPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from the Server
        String response = payload.toString();
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): received String Payload: {}", response);
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): id: {}, You were sent (STRING): {}", payload.getId(), response);
    }
    // Data Payload
    public static void sendData(DataPayload payload)
    {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveData(DataPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from server
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): received Nbt Payload (size in bytes): {}", payload.data().getSizeInBytes());
        PacketByteBuf buf = PayloadUtils.fromNbt(payload.data(), DataPayload.KEY);
        assert buf != null;
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): buf size in bytes: {}", buf.readableBytes());
        // --> To write a PacketByteBuf from NbtCompound
//        String response = payload.data().getString(NbtPayload.KEY);
        String response = buf.readString();
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): id: {}, String: {}", payload.getId(), response);

        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): You were sent (NBT-DATA): {}", response);
    }
    public static void sendCarpet(CarpetPayload payload)
    {
        // Server-bound packet sent from the Client
        // --> Carpet server present
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendCarpet(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveCarpet(CarpetPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from server
        // --> Carpet server present
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveCarpet(): id: {} received Carpet Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        // Handle Carpet packet
        ((CarpetHandler) CarpetHandler.getInstance()).receiveCarpetPayload(payload.data(), ctx);
    }
    public static void sendServUX(ServuxPayload payload)
    {
        // Client-bound packet sent from the Server
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendServUX(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveServUX(ServuxPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from the Server
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveServUX(): id: {} received ServUX Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());
        ((ServuxPayloadHandler) ServuxPayloadHandler.getInstance()).receiveServuxPayload(payload.data(), ctx, payload.getId().id());
    }

    public static void sendSyncmatica(SyncmaticaPayload payload)
    {
        // Client-bound packet sent from the Server
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendSyncmatica(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveSyncmatica(SyncmaticaPayload payload, ClientPlayNetworking.Context ctx)
    {
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveSyncmatica(): id: {} received ServUX Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());
        ((SyncmaticaPayloadHandler) SyncmaticaPayloadHandler.getInstance()).receiveSyncmaticaPayload(payload.data(), ctx, payload.getId().id());
    }
}
