package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.event.CarpetHandler;

public class PacketProvider
{
    static CarpetPayloadListener carpetListener = new CarpetPayloadListener();
    public static void registerPayloads()
    {
        // Register Client Payload Listeners
        CarpetHandler.getInstance().registerCarpetHandler(carpetListener);
    }

    public static void unregisterPayloads()
    {
        CarpetHandler.getInstance().unregisterCarpetHandler(carpetListener);
    }
}