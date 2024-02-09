package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxLitematicsPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxMetadataPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxStructuresPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworkPlayRegister
{
    static ClientPlayNetworking.PlayPayloadHandler<CarpetHelloPayload> S2CCarpetNbtHandler;
    static ClientPlayNetworking.PlayPayloadHandler<ServuxLitematicsPayload> S2CServuxLitematicsHandler;
    static ClientPlayNetworking.PlayPayloadHandler<ServuxMetadataPayload> S2CServuxMetadataHandler;
    static ClientPlayNetworking.PlayPayloadHandler<ServuxStructuresPayload> S2CServuxStructuresHandler;
    private static boolean receiversInit = false;
    public static void registerReceivers()
    {
        // Don't register more than once
        if (receiversInit)
            return;
        // Wait until world/server joined
        if (MaLiLibReference.isClient())
        {
            if (MaLiLibReference.isSinglePlayer())
                MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): Game is running in Single Player Mode.");
            MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): isClient() true.  Register handlers.");

            ClientPlayNetworking.registerGlobalReceiver(CarpetHelloPayload.TYPE, S2CCarpetNbtHandler);
            ClientPlayNetworking.registerGlobalReceiver(ServuxLitematicsPayload.TYPE, S2CServuxLitematicsHandler);
            ClientPlayNetworking.registerGlobalReceiver(ServuxMetadataPayload.TYPE, S2CServuxMetadataHandler);
            ClientPlayNetworking.registerGlobalReceiver(ServuxStructuresPayload.TYPE, S2CServuxStructuresHandler);
            receiversInit = true;
        }
    }

    public static void unregisterReceivers()
    {
        // Do when disconnecting from server/world
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): isClient() true.  Unregister handlers.");

            ClientPlayNetworking.unregisterGlobalReceiver(CarpetHelloPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(ServuxLitematicsPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(ServuxStructuresPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(ServuxMetadataPayload.TYPE.id());
            receiversInit = false;
        }
    }
    static
    {
        S2CCarpetNbtHandler = ClientNetworkPlayHandler::receiveCarpetHello;
        S2CServuxLitematicsHandler = ClientNetworkPlayHandler::receiveServuxLitematics;
        S2CServuxMetadataHandler = ClientNetworkPlayHandler::receiveServuxMetadata;
        S2CServuxStructuresHandler = ClientNetworkPlayHandler::receiveServuxStructures;
    }
}
