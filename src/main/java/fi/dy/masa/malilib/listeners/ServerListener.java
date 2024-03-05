package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.packet_example.PacketListenerRegister;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;
import fi.dy.masa.malilib.network.test.ServerDebugSuite;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

/**
 * This could be used on downstream mods, this is critical for the Network API.
 */
public class ServerListener implements IServerListener
{
    /**
     * This interface for IntegratedServers() works much more reliably than invoking a WorldLoadHandler
     * -- I've tried it first! --
     * The WorldLoadHandler calls Connect/Disconnect multiple times, breaking the networking API.
     * So using the IServerListener is best because it only gets invoked ONCE per a server start / stop
     * to get handled correctly.
     */

    @Override
    public void onServerStarting(MinecraftServer server)
    {
        if (server.isSingleplayer())
        {
            MaLiLibReference.setOpenToLan(false);
            MaLiLibReference.setDedicated(false);
            MaLiLib.printDebug("[{}] Single Player Mode detected", MaLiLibReference.MOD_ID);
        }
        else if (server.isDedicated())
        {
            MaLiLibReference.setDedicated(true);
            MaLiLib.printDebug("[{}] Dedicated Server Mode detected", MaLiLibReference.MOD_ID);
        }

        PacketListenerRegister.registerListeners();

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onServerStarted(MinecraftServer minecraftServer)
    {
        PayloadTypeRegister.getInstance().registerAllHandlers();

        if (MaLiLibReference.isClient())
        {
            ClientDebugSuite.checkGlobalConfigChannels();
            ClientDebugSuite.checkGlobalPlayChannels();
        }
        else
        {
            ServerDebugSuite.checkGlobalConfigChannels();
            ServerDebugSuite.checkGlobalPlayChannels();

            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
    }

    @Override
    public void onServerIntegratedSetup(IntegratedServer server)
    {
        MaLiLib.printDebug("[{}] Integrated Server Mode detected", MaLiLibReference.MOD_ID);
        MaLiLibReference.setIntegrated(true);
        MaLiLibReference.setOpenToLan(false);
        MaLiLibReference.setDedicated(false);
    }

    @Override
    public void onServerOpenToLan(IntegratedServer server)
    {
        MaLiLib.printDebug("[{}] OpenToLan Mode detected [Serving on localhost:{}]", MaLiLibReference.MOD_ID, server.getServerPort());
        MaLiLibReference.setIntegrated(true);
        MaLiLibReference.setOpenToLan(true);
        MaLiLibReference.setDedicated(false);

        PayloadTypeRegister.getInstance().resetPayloads();
        PayloadTypeRegister.getInstance().registerAllHandlers();
    }

    @Override
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        MaLiLib.printDebug("[{}] server is stopping", MaLiLibReference.MOD_ID);

        PayloadTypeRegister.getInstance().resetPayloads();

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
    }

    @Override
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        MaLiLib.printDebug("[{}] server has stopped", MaLiLibReference.MOD_ID);
        MaLiLibReference.setDedicated(false);
        MaLiLibReference.setIntegrated(false);
        MaLiLibReference.setOpenToLan(false);
    }
}
