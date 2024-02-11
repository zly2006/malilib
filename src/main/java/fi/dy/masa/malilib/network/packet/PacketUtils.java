package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.ClientPlayHandler;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;

/**
 * Example for PacketListeners for downstream mods, so I'm using a basic Carpet Hello Handler.
 */
public class PacketUtils
{
    static fi.dy.masa.malilib.network.packet.CarpetHelloPlayListener<CarpetHelloPayload> CarpetHelloPlayListener = fi.dy.masa.malilib.network.packet.CarpetHelloPlayListener.INSTANCE;
    private static boolean payloadsRegistered = false;
    public static void registerPayloads()
    {
        if (payloadsRegistered)
            return;
        MaLiLib.printDebug("PacketProvider#registerPayloads(): registerCarpetHandler()");
        ClientPlayHandler.getInstance().registerClientPlayHandler(CarpetHelloPlayListener);

        payloadsRegistered = true;
    }
    public static void unregisterPayloads()
    {
        MaLiLib.printDebug("PacketProvider#unregisterPayloads(): unregisterCarpetHandler()");
        ClientPlayHandler.getInstance().unregisterClientPlayHandler(CarpetHelloPlayListener);

        payloadsRegistered = false;
    }
}
