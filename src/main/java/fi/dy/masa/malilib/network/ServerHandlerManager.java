package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.C2SHandler;
import fi.dy.masa.malilib.network.payload.C2SDataPayload;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerHandlerManager
{
    static ServerPlayNetworking.PlayPayloadHandler<C2SStringPayload> C2SStringHandler;
    static ServerPlayNetworking.PlayPayloadHandler<C2SDataPayload> C2SDataHandler;
    
    public static void registerDefaultReceivers()
    {
        // Do when server starts
        if (MaLiLibReference.isServer())
        {
            MaLiLib.printDebug("ServerHandlerManager#registerDefaultReceivers(): isServer() true.");
            MaLiLib.printDebug("ServerHandlerManager#registerDefaultReceivers(): registerStringHandler()");

            ServerPlayNetworking.registerGlobalReceiver(C2SStringPayload.TYPE, C2SStringHandler);

            MaLiLib.printDebug("ServerHandlerManager#registerDefaultReceivers(): registerDataHandler()");
            ServerPlayNetworking.registerGlobalReceiver(C2SDataPayload.TYPE, C2SDataHandler);

            MaLiLib.printDebug("ServerHandlerManager#registerDefaultReceivers(): END.");
        }
    }

    public static void unregisterDefaultReceivers()
    {
        // Do when server stops
        if (MaLiLibReference.isServer())
        {
            MaLiLib.printDebug("ServerHandlerManager#unregisterDefaultReceivers(): isServer() true.");
            MaLiLib.printDebug("ServerHandlerManager#unregisterDefaultReceivers(): registerStringHandler()");

            ServerPlayNetworking.unregisterGlobalReceiver(C2SStringPayload.TYPE.id());

            MaLiLib.printDebug("ServerHandlerManager#unregisterDefaultReceivers(): registerDataHandler()");
            ServerPlayNetworking.unregisterGlobalReceiver(C2SDataPayload.TYPE.id());

            MaLiLib.printDebug("ServerHandlerManager#unregisterDefaultReceivers(): END.");
        }
    }
    static
    {
        C2SStringHandler = C2SHandler::receive;
        C2SDataHandler = C2SHandler::receive;
    }
}
