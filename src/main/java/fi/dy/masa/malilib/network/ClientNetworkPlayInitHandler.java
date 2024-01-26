package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;

public class ClientNetworkPlayInitHandler
{
    /**
     * Should be called when Client opens the main screen
     */
    public static void registerPlayChannels()
    {
        PayloadTypeRegister.registerDefaultTypes(MaLiLibReference.COMMON_NAMESPACE);
        PayloadTypeRegister.registerDefaultPlayChannels();
        ClientDebugSuite.checkGlobalChannels();
    }
    /**
     * Should be called when Client joins a server
     */
    public static void registerReceivers()
    {
        ClientNetworkPlayRegister.registerDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
    public static void unregisterReceivers()
    {
        ClientNetworkPlayRegister.unregisterDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
}
