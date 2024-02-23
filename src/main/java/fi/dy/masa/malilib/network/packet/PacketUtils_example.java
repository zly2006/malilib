package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.play.ClientPlayHandler;
import fi.dy.masa.malilib.network.payload.channel.*;

/**
 * Example for PacketListeners for downstream mods, so I'm using a basic Carpet Hello Handler.
 */
public class PacketUtils_example
{
    static CarpetHelloPlayListener_example<CarpetHelloPayload> CarpetHelloListener = CarpetHelloPlayListener_example.INSTANCE;
    private static boolean payloadsRegistered = false;
    public static void registerPayloads()
    {
        if (payloadsRegistered)
            return;
        MaLiLib.printDebug("PacketProvider#registerPayloads(): registerCarpetHandler()");
        ClientPlayHandler.getInstance().registerClientPlayHandler(CarpetHelloListener);

        payloadsRegistered = true;
    }
    public static void unregisterPayloads()
    {
        MaLiLib.printDebug("PacketProvider#unregisterPayloads(): unregisterCarpetHandler()");
        ClientPlayHandler.getInstance().unregisterClientPlayHandler(CarpetHelloListener);

        payloadsRegistered = false;
    }
}
