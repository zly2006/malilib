package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.packet.PacketProvider;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;

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
        PacketProvider.registerPayloads();
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
