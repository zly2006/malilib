package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.packet.PacketUtils;
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
        PacketUtils.registerPayloads();
        //((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayPayload(PayloadType.CARPET_HELLO);
        //((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).registerConfigPayload(PayloadType.CARPET_HELLO);

        //ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStarting(): invoked.");
    }
    public void onServerStarted(MinecraftServer minecraftServer)
    {
        // PacketUtils is responsible for registering the Handlers

        PayloadTypeRegister.getInstance().registerAllHandlers();
        //((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayHandler(PayloadType.CARPET_HELLO);
        //((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).registerConfigHandler(PayloadType.CARPET_HELLO);
        ClientDebugSuite.checkGlobalChannels();

        MaLiLib.printDebug("MinecraftServerEvents#onServerStarted(): invoked.");
    }
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        PayloadTypeRegister.getInstance().resetPayloads();
        // This sends a global reset() to all network Handler's.
        ClientDebugSuite.checkGlobalChannels();

        MaLiLib.printDebug("MinecraftServerEvents#onServerStopping(): invoked.");
    }
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        //ClientDebugSuite.checkGlobalChannels();
        MaLiLib.printDebug("MinecraftServerEvents#onServerStopped(): invoked.");
    }
}
