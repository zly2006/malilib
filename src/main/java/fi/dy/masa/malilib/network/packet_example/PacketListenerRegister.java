package fi.dy.masa.malilib.network.packet_example;

import fi.dy.masa.malilib.MaLiLibReference;

/**
 * Example for PacketListeners for downstream mods, so I'm using a basic Carpet Hello Handler here
 */
public class PacketListenerRegister
{
    //static CarpetHelloPlayListener<CarpetHelloPayload> CarpetHelloListener = CarpetHelloPlayListener.INSTANCE;
    private static boolean listenersRegistered = false;
    public static void registerListeners()
    {
        if (listenersRegistered)
            return;
        if (MaLiLibReference.isClient())
        {
            //MaLiLib.printDebug("PacketProvider#registerPayloads(): registerCarpetHandler()");
            //ClientPlayHandler.getInstance().registerClientPlayHandler(CarpetHelloListener);
        }

        listenersRegistered = true;
    }
    public static void unregisterListeners()
    {
        if (MaLiLibReference.isClient())
        {
            //MaLiLib.printDebug("PacketProvider#unregisterPayloads(): unregisterCarpetHandler()");
            //ClientPlayHandler.getInstance().unregisterClientPlayHandler(CarpetHelloListener);
        }
        listenersRegistered = false;
    }
}
