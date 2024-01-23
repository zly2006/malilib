package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.S2CHandler;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientHandlerManager
{
    static ClientPlayNetworking.PlayPayloadHandler<S2CStringPayload> S2CStringHandler;
    static ClientPlayNetworking.PlayPayloadHandler<S2CDataPayload> S2CDataHandler;

    public static void registerDefaultReceivers()
    {
        // Wait until world/server joined
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): isClient() true.");
            MaLiLib.printDebug("ClientHandlerManager#registerDefaultReceivers(): registerStringHandler()");

            ClientPlayNetworking.registerGlobalReceiver(S2CStringPayload.TYPE, S2CStringHandler);

            MaLiLib.printDebug("PayloadTypes#registerDefaultReceivers(): registerDataHandler()");
            ClientPlayNetworking.registerGlobalReceiver(S2CDataPayload.TYPE, S2CDataHandler);

            MaLiLib.printDebug("PayloadTypes#registerDefaultReceivers(): END.");
        }
    }

    public static void unregisterDefaultReceivers()
    {
        // Do when disconnecting from server/world
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): isClient() true.");
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): registerStringHandler()");

            ClientPlayNetworking.unregisterGlobalReceiver(S2CStringPayload.TYPE.id());

            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): registerDataHandler()");
            ClientPlayNetworking.unregisterGlobalReceiver(S2CDataPayload.TYPE.id());

            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): END.");
        }
    }
    static
    {
        S2CStringHandler = S2CHandler::receive;
        S2CDataHandler = S2CHandler::receive;
    }
}
