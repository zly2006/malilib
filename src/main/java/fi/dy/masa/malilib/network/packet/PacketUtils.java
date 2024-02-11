package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.ClientPlayHandler;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;

/**
 * Example for PacketListeners for downstream mods, so I'm using a basic Carpet Hello Handler.
 */
public class PacketUtils
{
    static CarpetHelloPlayListener<CarpetHelloPayload> CarpetHelloistener = CarpetHelloPlayListener.INSTANCE;
    private static boolean payloadsRegistered = false;
    public static void registerPayloads()
    {
        if (payloadsRegistered)
            return;
        MaLiLib.printDebug("PacketProvider#registerPayloads(): registerCarpetHandler()");
        ClientPlayHandler.getInstance().registerClientPlayHandler(CarpetHelloistener);

        payloadsRegistered = true;
    }
    public static void unregisterPayloads()
    {
        MaLiLib.printDebug("PacketProvider#unregisterPayloads(): unregisterCarpetHandler()");
        ClientPlayHandler.getInstance().unregisterClientPlayHandler(CarpetHelloistener);

        payloadsRegistered = false;
    }
}
