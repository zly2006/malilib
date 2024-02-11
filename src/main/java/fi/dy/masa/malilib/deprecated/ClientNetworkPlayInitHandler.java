package fi.dy.masa.malilib.deprecated;

import fi.dy.masa.malilib.MaLiLib;

/**
 * Perhaps this can be made more abstract or simplified
 */
@Deprecated
public class ClientNetworkPlayInitHandler
{
    /**
     * Should be called when Client opens the main screen
     */
    public static void registerPlayChannels()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#registerPlayChannels(): called.");
        //FIXME PayloadTypeRegister.registerTypes(MaLiLibReference.COMMON_NAMESPACE);
        //FIXME PayloadTypeRegister.registerPlayChannels();
        //FIXME PacketUtils_example.registerPayloads();
        //ClientDebugSuite.checkGlobalChannels();
    }
    /**
     * Should be called when Client joins a server
     */
    public static void registerReceivers()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#registerReceivers(): called.");
        // FIXME ClientCommonHandlerRegister.registerReceivers();
        //ClientDebugSuite.checkGlobalChannels();
    }
    public static void unregisterReceivers()
    {
        MaLiLib.printDebug("ClientNetworkPlayInitHandler#unregisterReceivers(): called.");
        // FIXME ClientCommonHandlerRegister.unregisterReceivers();
        //ClientDebugSuite.checkGlobalChannels();
    }
}
