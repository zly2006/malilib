package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.network.ServerNetworkPlayRegister;

public class ServerEvents {
    // See TestSuite init()
    protected static void started()
    {
        ServerNetworkPlayRegister.registerDefaultReceivers();
        ServerDebugSuite.checkGlobalChannels();
    }
    protected static void stopping()
    {
        ServerNetworkPlayRegister.unregisterDefaultReceivers();
        ServerDebugSuite.checkGlobalChannels();
    }
}
