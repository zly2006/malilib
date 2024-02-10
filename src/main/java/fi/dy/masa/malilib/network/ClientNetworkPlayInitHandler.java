package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.packet.PacketUtils;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;

/**
 * Perhaps this can be made more abstract or simplified
 */
public class ClientNetworkPlayInitHandler
{
    /**
     * Should be called when Client opens the main screen
     */
    public static void registerPlayChannels()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#registerPlayChannels(): called.");
        PayloadTypeRegister.registerTypes(MaLiLibReference.COMMON_NAMESPACE);
        PayloadTypeRegister.registerPlayChannels();
        PacketUtils.registerPayloads();
        ClientDebugSuite.checkGlobalChannels();
    }
    /**
     * Should be called when Client joins a server
     */
    public static void registerReceivers()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#registerReceivers(): called.");
        ClientNetworkPlayRegister.registerReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
    public static void unregisterReceivers()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#unregisterReceivers(): called.");
        ClientNetworkPlayRegister.unregisterReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
}
