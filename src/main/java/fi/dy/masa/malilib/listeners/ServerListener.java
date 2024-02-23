package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.packet.PacketUtils_example;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.test.ClientDebugSuite;
import fi.dy.masa.malilib.network.test.ServerDebugSuite;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

/**
 * This should be moved to the downstream mods, this is just an example of how you can launch the Network API
 */
public class ServerListener implements IServerListener
{
    /**
     * This interface for IntegratedServers() works much more reliably than invoking a WorldLoadHandler
     * -- I've tried it first! --
     * The WorldLoadHandler calls Connect/Disconnect multiple times, breaking the networking API.
     * So using the IServerListener is best because it only gets invoked ONCE per a server start / stop.
     */

    @Override
    public void onServerStarting(MinecraftServer minecraftServer)
    {
        if (minecraftServer.isDedicated())
        {
            MaLiLibReference.setDedicated(true);
            MaLiLib.logger.info("MaLiLib Dedicated Server Mode detected.");
        }

        // PayloadTypeRegister is responsible for registering *ALL* of the Payloads
        PacketUtils_example.registerPayloads();

        if (MaLiLibReference.isClient())
        {
            //ClientDebugSuite.checkGlobalConfigChannels();
            //ClientDebugSuite.checkGlobalPlayChannels();
        }
        else
        {
            //ServerDebugSuite.checkGlobalConfigChannels();
            //ServerDebugSuite.checkGlobalPlayChannels();

            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onServerStarted(MinecraftServer minecraftServer)
    {
        // PayloadTypeRegister is responsible for registering *ALL* of the Handlers
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
        MaLiLib.logger.info("MaLiLib Integrated Server Mode detected.");
        MaLiLibReference.setIntegrated(true);
    }

    @Override
    public void onServerOpenToLan(IntegratedServer server)
    {
        MaLiLib.logger.info("MaLiLib OpenToLan Mode detected.");
        MaLiLibReference.setOpenToLan(true);

        PayloadTypeRegister.getInstance().resetPayloads();
        PayloadTypeRegister.getInstance().registerAllHandlers();
    }

    @Override
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        // This sends a global reset() to all network Handler's.
        PayloadTypeRegister.getInstance().resetPayloads();

        if (MaLiLibReference.isClient())
        {
            //ClientDebugSuite.checkGlobalConfigChannels();
            //ClientDebugSuite.checkGlobalPlayChannels();
        }
        else
        {
            //ServerDebugSuite.checkGlobalConfigChannels();
            //ServerDebugSuite.checkGlobalPlayChannels();

            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
    }
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        if (MaLiLibReference.isDedicated())
            MaLiLibReference.setDedicated(false);
        if (MaLiLibReference.isIntegrated())
            MaLiLibReference.setIntegrated(false);
        if (MaLiLibReference.isOpenToLan())
            MaLiLibReference.setOpenToLan(false);

        if (MaLiLibReference.isClient())
        {
            //ClientDebugSuite.checkGlobalConfigChannels();
            //ClientDebugSuite.checkGlobalPlayChannels();
        }
        else
        {
            //ServerDebugSuite.checkGlobalConfigChannels();
            //ServerDebugSuite.checkGlobalPlayChannels();
        }
    }
}
