package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.ClientNetworkPlayInitHandler;
import fi.dy.masa.malilib.network.ClientNetworkPlayRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;
import net.minecraft.server.MinecraftServer;

public class ServerListener implements IServerListener
{
    /**
     * This interface for IntegratedServers() works much more reliably than invoking a WorldLoadHandler
     * @param minecraftServer
     */

    public void onServerStarting(MinecraftServer minecraftServer)
    {
        ClientNetworkPlayInitHandler.registerPlayChannels();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarting(): invoked.");
    }
    public void onServerStarted(MinecraftServer minecraftServer)
    {
        ClientNetworkPlayRegister.registerDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarted(): invoked.");
    }
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStopping(): invoked.");
    }
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        ClientNetworkPlayRegister.unregisterDefaultReceivers();
        ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStopped(): invoked.");
    }
}
