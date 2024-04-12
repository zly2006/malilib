package fi.dy.masa.malilib;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadManager;

/**
 * This could be used on downstream mods, such as ServuX.
 * This is critical for the Network API to function properly at the correct timings,
 * and to help manage ModInitTasks in a Server Environment.
 */
public class MaLiLibServerListener implements IServerListener
{
    private InetAddress localIpAddr = null;

    @Override
    public InetAddress getLocalIpAddr()
    {
        if (this.localIpAddr == null)
        {
            try
            {
                this.localIpAddr = InetAddress.getLocalHost();
            }
            catch (UnknownHostException e)
            {
                this.localIpAddr = InetAddress.getLoopbackAddress();
            }
        }

        return this.localIpAddr;
    }

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

        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onServerStarted(MinecraftServer server)
    {
        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }

        PayloadManager.getInstance().verifyPayloads();
        PayloadManager.getInstance().registerHandlers();

        if (MaLiLibReference.isDedicated())
        {
            if (this.localIpAddr == null)
            {
                this.localIpAddr = getLocalIpAddr();
            }

            String ipPortString = "["+this.localIpAddr.getHostName()+"] "+this.localIpAddr.getHostAddress() +":"+ server.getServerPort();
            MaLiLib.logger.info("[{}] Dedicated server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
        }
    }

    @Override
    public void onServerIntegratedSetup(IntegratedServer server)
    {
        MaLiLibReference.setOpenToLan(false);
        MaLiLibReference.setDedicated(false);

        if (this.localIpAddr == null)
        {
            this.localIpAddr = getLocalIpAddr();
        }
    }

    @Override
    public void onServerOpenToLan(IntegratedServer server)
    {
        if (this.localIpAddr == null)
        {
            this.localIpAddr = getLocalIpAddr();
        }

        MaLiLibReference.setOpenToLan(true);
        MaLiLibReference.setDedicated(false);

        PayloadManager.getInstance().resetPayloads();
        PayloadManager.getInstance().verifyPayloads();
        PayloadManager.getInstance().registerHandlers();

        String ipPortString = "["+this.localIpAddr.getHostName()+"] "+this.localIpAddr.getHostAddress() +":"+ server.getServerPort();
        MaLiLib.logger.info("[{}] OpenToLan server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
    }

    @Override
    public void onServerStopping(MinecraftServer minecraftServer)
    {
        PayloadManager.getInstance().resetPayloads();

        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }
    }

    @Override
    public void onServerStopped(MinecraftServer minecraftServer)
    {
        MaLiLibReference.setDedicated(false);
        MaLiLibReference.setOpenToLan(false);
        this.localIpAddr = null;
    }
}
