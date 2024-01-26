package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.event.CarpetHandler;
import fi.dy.masa.malilib.event.ServuxPayloadHandler;

public class PacketProvider
{
    static CarpetPayloadListener carpetListener = new CarpetPayloadListener();
    static ServuxPayloadListener servuxListener = new ServuxPayloadListener();
    public static void registerPayloads()
    {
        // Register Client Payload Listeners
        CarpetHandler.getInstance().registerCarpetHandler(carpetListener);
        ServuxPayloadHandler.getInstance().registerServuxHandler(servuxListener);
    }

    public static void unregisterPayloads()
    {
        CarpetHandler.getInstance().unregisterCarpetHandler(carpetListener);
        ServuxPayloadHandler.getInstance().unregisterServuxHandler(servuxListener);
    }
}