package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworkPlayRegister
{
    //static ClientPlayNetworking.PlayPayloadHandler<StringPayload> S2CStringHandler;
    //static ClientPlayNetworking.PlayPayloadHandler<DataPayload> S2CDataHandler;
    static ClientPlayNetworking.PlayPayloadHandler<CarpetPayload> S2CCarpetNbtHandler;
    static ClientPlayNetworking.PlayPayloadHandler<ServuxPayload> S2CServUXHandler;
    static ClientPlayNetworking.PlayPayloadHandler<SyncmaticaPayload> S2CSyncmaticaHandler;
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
            //ClientPlayNetworking.registerGlobalReceiver(StringPayload.TYPE, S2CStringHandler);
            //ClientPlayNetworking.registerGlobalReceiver(DataPayload.TYPE, S2CDataHandler);
            ClientPlayNetworking.registerGlobalReceiver(CarpetPayload.TYPE, S2CCarpetNbtHandler);
            ClientPlayNetworking.registerGlobalReceiver(ServuxPayload.TYPE, S2CServUXHandler);
            ClientPlayNetworking.registerGlobalReceiver(SyncmaticaPayload.TYPE, S2CSyncmaticaHandler);
            receiversInit = true;
        }
    }

    public static void unregisterReceivers()
    {
        // Do when disconnecting from server/world
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("ClientHandlerManager#unregisterDefaultReceivers(): isClient() true.  Unregister handlers.");
            //ClientPlayNetworking.unregisterGlobalReceiver(StringPayload.TYPE.id());
            //ClientPlayNetworking.unregisterGlobalReceiver(DataPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(CarpetPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(ServuxPayload.TYPE.id());
            ClientPlayNetworking.unregisterGlobalReceiver(SyncmaticaPayload.TYPE.id());
            receiversInit = false;
        }
    }
    static
    {
        //S2CStringHandler = ClientNetworkPlayHandler::receiveString;
        //S2CDataHandler = ClientNetworkPlayHandler::receiveData;
        S2CCarpetNbtHandler = ClientNetworkPlayHandler::receiveCarpet;
        S2CServUXHandler = ClientNetworkPlayHandler::receiveServUX;
        S2CSyncmaticaHandler = ClientNetworkPlayHandler::receiveSyncmatica;
    }
}
