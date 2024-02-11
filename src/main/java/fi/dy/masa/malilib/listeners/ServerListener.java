package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.packet.PacketUtils_example;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;
import net.minecraft.server.MinecraftServer;

/**
 * This should be moved to the downstream mods, this is just an example of how you can launch the Network API
 */
public class ServerListener implements IServerListener
{
    /**
     * This interface for IntegratedServers() works much more reliably than invoking a WorldLoadHandler
     * --> I've tried it at first --
     * The WorldLoadHandler calls Connect/Disconnect multiple times, breaking the networking API,
     * so using the IServerListener is best because it only gets invoked ONCE.
     */

    public void onServerStarting(MinecraftServer minecraftServer)
    {
        PacketUtils_example.registerPayloads();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarting(): invoked.");

        ClientDebugSuite.checkGlobalPlayChannels();
        ClientDebugSuite.checkGlobalConfigChannels();
    }
    public void onServerStarted(MinecraftServer minecraftServer)
    {
        // PayloadTypeRegister is responsible for registering *ALL* of the Handlers
        PayloadTypeRegister.getInstance().registerAllHandlers();

        ClientDebugSuite.checkGlobalPlayChannels();
        ClientDebugSuite.checkGlobalConfigChannels();

        MaLiLib.printDebug("MinecraftServerEvents#onServerStarted(): invoked.");
    }
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        PayloadTypeRegister.getInstance().resetPayloads();
        // This sends a global reset() to all network Handler's.

        ClientDebugSuite.checkGlobalPlayChannels();
        ClientDebugSuite.checkGlobalConfigChannels();

        MaLiLib.printDebug("MinecraftServerEvents#onServerStopping(): invoked.");
    }
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        ClientDebugSuite.checkGlobalPlayChannels();
        ClientDebugSuite.checkGlobalConfigChannels();

        MaLiLib.printDebug("MinecraftServerEvents#onServerStopped(): invoked.");
    }
}
