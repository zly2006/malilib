package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.CarpetPayload;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworkPlayRegister
{
    static ClientPlayNetworking.PlayPayloadHandler<S2CStringPayload> S2CStringHandler;
    static ClientPlayNetworking.PlayPayloadHandler<S2CDataPayload> S2CDataHandler;
    static ClientPlayNetworking.PlayPayloadHandler<CarpetPayload> CarpetNbtHandler;

    public static void registerDefaultReceivers()
    {
        // Wait until world/server joined
        if (MaLiLibReference.isClient())
        {
            if (MaLiLibReference.isSinglePlayer())
                MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): Game is running in Single Player Mode.");
            MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): isClient() true.  Register handlers.");
            ClientPlayNetworking.registerGlobalReceiver(S2CStringPayload.TYPE, S2CStringHandler);
            ClientPlayNetworking.registerGlobalReceiver(S2CDataPayload.TYPE, S2CDataHandler);
            ClientPlayNetworking.registerGlobalReceiver(CarpetPayload.TYPE, CarpetNbtHandler);
        }
    }

    public static void unregisterDefaultReceivers()
    {
        // Do when disconnecting from server/world
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): isClient() true.  Unregister handlers.");
            ClientPlayNetworking.unregisterGlobalReceiver(S2CStringPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(S2CDataPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(CarpetPayload.TYPE.id());
        }
    }
    static
    {
        S2CStringHandler = ClientNetworkPlayHandler::receive;
        S2CDataHandler = ClientNetworkPlayHandler::receive;
        CarpetNbtHandler = ClientNetworkPlayHandler::receiveCarpet;
    }
}
