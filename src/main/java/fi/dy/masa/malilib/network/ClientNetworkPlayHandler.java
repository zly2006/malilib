package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.event.CarpetHelloHandler;
import fi.dy.masa.malilib.event.ServuxLitematicsHandler;
import fi.dy.masa.malilib.event.ServuxMetadataHandler;
import fi.dy.masa.malilib.event.ServuxStructuresHandler;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxLitematicsPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxMetadataPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxStructuresPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Probably can make this abstract, but it needs to be mapped as static, so I made a secondary abstract layer under the "packet" folder via PacketUtils.
 */

/** NOTES
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
    public static void sendCarpetHello(CarpetHelloPayload payload)
    {
        // Server-bound packet sent from the Client
        // --> Carpet server present
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendCarpetHello(): sending payload id: {}", payload.getId().id());
        }
    }
    public static void sendServuxLitematics(ServuxLitematicsPayload payload)
    {
        // Client-bound packet sent from the Server
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendServuxLitematics(): sending payload id: {}", payload.getId().id());
        }
    }

    public static void sendServuxMetadata(ServuxMetadataPayload payload)
    {
        // Client-bound packet sent from the Server
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendServuxMetadata(): sending payload id: {}", payload.getId().id());
        }
    }
    public static void sendServuxStructures(ServuxStructuresPayload payload)
    {
        // Client-bound packet sent from the Server
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendServuxStructures(): sending payload id: {}", payload.getId().id());
        }
    }
    public static void receiveCarpetHello(CarpetHelloPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from server
        // --> Carpet server present
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveCarpetHello(): id: {} received Carpet Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        // Handle Carpet packet
        ((CarpetHelloHandler) CarpetHelloHandler.getInstance()).receiveCarpetHello(payload.data(), ctx);
    }
    public static void receiveServuxLitematics(ServuxLitematicsPayload payload, ClientPlayNetworking.Context ctx)
    {
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveServuxLitematics(): id: {} received Servux Litematics Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        ((ServuxLitematicsHandler) ServuxLitematicsHandler.getInstance()).receiveServuxLitematics(payload.data(), ctx, payload.getId().id());
    }
    public static void receiveServuxMetadata(ServuxMetadataPayload payload, ClientPlayNetworking.Context ctx)
    {
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveServuxMetadata(): id: {} received Servux Metadata Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        ((ServuxMetadataHandler) ServuxMetadataHandler.getInstance()).receiveServuxMetadata(payload.data(), ctx, payload.getId().id());
    }
    public static void receiveServuxStructures(ServuxStructuresPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from the Server
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveServuxStructures(): id: {} received Servux Structures Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        ((ServuxStructuresHandler) ServuxStructuresHandler.getInstance()).receiveServuxStructures(payload.data(), ctx, payload.getId().id());
    }
}
