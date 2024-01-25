package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworkPlayRegister
{
    static ClientPlayNetworking.PlayPayloadHandler<StringPayload> S2CStringHandler;
    static ClientPlayNetworking.PlayPayloadHandler<DataPayload> S2CDataHandler;
    static ClientPlayNetworking.PlayPayloadHandler<CarpetPayload> S2CCarpetNbtHandler;

    public static void registerDefaultReceivers()
    {
        // Wait until world/server joined
        if (MaLiLibReference.isClient())
        {
            if (MaLiLibReference.isSinglePlayer())
                MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): Game is running in Single Player Mode.");
            MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): isClient() true.  Register handlers.");
            ClientPlayNetworking.registerGlobalReceiver(StringPayload.TYPE, S2CStringHandler);
            ClientPlayNetworking.registerGlobalReceiver(DataPayload.TYPE, S2CDataHandler);
            ClientPlayNetworking.registerGlobalReceiver(CarpetPayload.TYPE, S2CCarpetNbtHandler);
        }
    }

    public static void unregisterDefaultReceivers()
    {
        // Do when disconnecting from server/world
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): isClient() true.  Unregister handlers.");
            ClientPlayNetworking.unregisterGlobalReceiver(StringPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(DataPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(CarpetPayload.TYPE.id());
        }
    }
    static
    {
        S2CStringHandler = ClientNetworkPlayHandler::receive;
        S2CDataHandler = ClientNetworkPlayHandler::receive;
        S2CCarpetNbtHandler = ClientNetworkPlayHandler::receiveCarpet;
    }
}
