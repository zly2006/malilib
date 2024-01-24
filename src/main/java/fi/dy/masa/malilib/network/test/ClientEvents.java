package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.network.ClientNetworkPlayRegister;

public class ClientEvents {

    // SEE MixinMinecraftClient
    public static void joinWorld()
    {
        ClientNetworkPlayRegister.registerDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
    public static void leaveWorld()
    {
        ClientNetworkPlayRegister.unregisterDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
    }
}
