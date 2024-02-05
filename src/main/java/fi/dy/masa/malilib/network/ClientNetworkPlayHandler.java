package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.event.CarpetHandler;
import fi.dy.masa.malilib.event.ServuxPayloadHandler;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * canSend()
 * Wraps: canSend(payload.getId().id());
 * -> Wraps Internally as:
 * `--> ClientNetworkingImpl.getAddon(MinecraftClient.getInstance().getNetworkHandler()).getSendableChannels().contains(payload.getId().id());
 * send()
 * Wraps internally as:
 * --> MinecraftClient.getInstance().getNetworkHandler().sendPacket();
 */
public class ClientNetworkPlayHandler
{
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
}
