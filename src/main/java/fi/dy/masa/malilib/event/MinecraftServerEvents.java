package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.ClientNetworkPlayRegister;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;
import net.minecraft.server.MinecraftServer;

public class MinecraftServerEvents {
    public static void onServerStarting(MinecraftServer minecraftServer)
    {
        PayloadTypeRegister.initTypes("servux");
        PayloadTypeRegister.registerPlayChannels();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarting(): invoked.");
    }
    public static void onServerStarted(MinecraftServer minecraftServer)
    {
        ClientNetworkPlayRegister.registerDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarted(): invoked.");
    }
    public static void onServerStopping(MinecraftServer minecraftServer)
    {
        ClientNetworkPlayRegister.unregisterDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStopping(): invoked.");
    }
    public static void onServerStopped(MinecraftServer minecraftServer)
    {
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStopped(): invoked.");
    }
}
