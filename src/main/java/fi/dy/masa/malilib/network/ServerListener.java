package fi.dy.masa.malilib.network;

import java.net.InetAddress;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadManager;

/**
 * This could be used on downstream mods, such as ServuX.
 * This is critical for the Network API to function properly at the correct timings,
 * and to help manage ModInitTasks in a Server Environment.
 */
public class ServerListener implements IServerListener
{
    @Override
    public void onServerStarting(MinecraftServer server)
    {
        if (server.isSingleplayer())
        {
            NetworkReference.getInstance().setOpenToLan(false);
            NetworkReference.getInstance().setDedicated(false);
        }
        else if (server.isDedicated())
        {
            NetworkReference.getInstance().setDedicated(true);
        }

        PayloadManager.getInstance().verifyPayloads();
    }

    @Override
    public void onServerStarted(MinecraftServer server)
    {
        if (NetworkReference.getInstance().isDedicated())
        {
            InetAddress localIpAddr = NetworkReference.getInstance().getLocalIpAddr();

            String ipPortString = "["+localIpAddr.getHostName()+"] "+localIpAddr.getHostAddress() +":"+ server.getServerPort();
            MaLiLib.logger.info("[{}] Dedicated server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
        }
    }

    @Override
    public void onServerIntegratedSetup(IntegratedServer server)
    {
        NetworkReference.getInstance().setIntegrated(true);
        NetworkReference.getInstance().setOpenToLan(false);
        NetworkReference.getInstance().setDedicated(false);
        NetworkReference.getInstance().getLocalIpAddr();
    }

    @Override
    public void onServerOpenToLan(IntegratedServer server)
    {
        NetworkReference.getInstance().setOpenToLan(true);
        NetworkReference.getInstance().setIntegrated(true);
        NetworkReference.getInstance().setDedicated(false);

        PayloadManager.getInstance().resetPayloads();
        PayloadManager.getInstance().verifyPayloads();
        PayloadManager.getInstance().registerHandlers();

        InetAddress localIpAddr = NetworkReference.getInstance().getLocalIpAddr();

        String ipPortString = "["+localIpAddr.getHostName()+"] "+localIpAddr.getHostAddress() +":"+ server.getServerPort();
        MaLiLib.logger.info("[{}] OpenToLan server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
    }

    @Override
    public void onServerStopping(MinecraftServer server)
    {
        PayloadManager.getInstance().resetPayloads();
    }

    @Override
    public void onServerStopped(MinecraftServer server)
    {
        NetworkReference.getInstance().setDedicated(false);
        NetworkReference.getInstance().setOpenToLan(false);
        NetworkReference.getInstance().setIntegrated(false);
        NetworkReference.getInstance().setLocalIpAddr(null);

        PayloadManager.getInstance().unregisterHandlers();
    }
}