package fi.dy.masa.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import java.net.*;

/**
 * This could be used on downstream mods, such as ServUX.
 * This is critical for the Network API to function properly at the correct timings.
 */
public class MaLiLibServerListener implements IServerListener
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
        }
        else if (server.isDedicated())
        {
            MaLiLibReference.setDedicated(true);
        }

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onServerStarted(MinecraftServer server)
    {
        PayloadManager.getInstance().verifyAllPayloads();
        PayloadManager.getInstance().registerAllHandlers();

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
        if (MaLiLibReference.isDedicated())
        {
            InetAddress localIpAddr;
            String ipPortString;
            try
            {
                localIpAddr = InetAddress.getLocalHost();
                ipPortString = "["+localIpAddr.getHostName()+"] "+localIpAddr.getHostAddress() +":"+ server.getServerPort();
            }
            catch (UnknownHostException e)
            {
                ipPortString = "localhost:"+ server.getServerPort();
            }
            MaLiLib.printDebug("[{}] Dedicated server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
        }
    }

    @Override
    public void onServerIntegratedSetup(IntegratedServer server)
    {
        MaLiLibReference.setOpenToLan(false);
        MaLiLibReference.setDedicated(false);
    }

    @Override
    public void onServerOpenToLan(IntegratedServer server)
    {
        InetAddress localIpAddr;
        String ipPortString;
        try
        {
            localIpAddr = InetAddress.getLocalHost();
            ipPortString = "["+localIpAddr.getHostName()+"] "+localIpAddr.getHostAddress() +":"+ server.getServerPort();
        }
        catch (UnknownHostException e)
        {
            ipPortString = "localhost:"+ server.getServerPort();
        }
        MaLiLib.printDebug("[{}] OpenToLan server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
        MaLiLibReference.setOpenToLan(true);
        MaLiLibReference.setDedicated(false);

        PayloadManager.getInstance().resetPayloads();
        PayloadManager.getInstance().verifyAllPayloads();
        PayloadManager.getInstance().registerAllHandlers();
    }

    @Override
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        PayloadManager.getInstance().resetPayloads();

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
    }

    @Override
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        MaLiLibReference.setDedicated(false);
        MaLiLibReference.setOpenToLan(false);
    }
}
