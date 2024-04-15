package fi.dy.masa.malilib.event;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadManager;

/**
 * Interface Handler for Server loading / unloading events --> similar to WorldLoadHandler,
 * but it only executes once at the proper time to register packet receivers, etc.
 */
public class ServerHandler implements IServerManager
{
    private static final ServerHandler INSTANCE = new ServerHandler();
    private final List<IServerListener> handlers = new ArrayList<>();
    public static IServerManager getInstance() { return INSTANCE; }

    private boolean dedicated_server = false;
    private boolean open_to_lan = false;
    private InetAddress localIpAddr = null;

    @Override
    public void registerServerHandler(IServerListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterServerHandler(IServerListener handler)
    {
        this.handlers.remove(handler);
    }

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
    public boolean isDedicated() { return this.dedicated_server; }

    @Override
    public boolean isOpenToLan() { return this.open_to_lan; }

    protected void setDedicated(boolean toggle)
    {
        if (toggle && MaLiLibReference.isServer())
        {
            this.dedicated_server = true;
            this.open_to_lan = false;
        }
        else
        {
            this.dedicated_server = false;
        }
    }

    protected void setOpenToLan(boolean toggle)
    {
        if (toggle && MaLiLibReference.isClient())
        {
            this.open_to_lan = true;
            this.dedicated_server = false;
        }
        else
        {
            this.open_to_lan = false;
        }
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerStarting(MinecraftServer server)
    {
        if (server.isSingleplayer())
        {
            this.setOpenToLan(false);
            this.setDedicated(false);
        }
        else if (server.isDedicated())
        {
            this.setDedicated(true);
        }

        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerStarting(server);
            }
        }
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerStarted(MinecraftServer server)
    {
        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }

        PayloadManager.getInstance().verifyPayloads();
        PayloadManager.getInstance().registerHandlers();

        if (this.isDedicated())
        {
            if (this.localIpAddr == null)
            {
                this.localIpAddr = getLocalIpAddr();
            }

            String ipPortString = "["+this.localIpAddr.getHostName()+"] "+this.localIpAddr.getHostAddress() +":"+ server.getServerPort();
            MaLiLib.logger.info("[{}] Dedicated server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);
        }

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerStarted(server);
            }
        }
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerIntegratedSetup(IntegratedServer server)
    {
        this.setOpenToLan(false);
        this.setDedicated(false);

        if (this.localIpAddr == null)
        {
            this.localIpAddr = getLocalIpAddr();
        }

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerIntegratedSetup(server);
            }
        }
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerOpenToLan(IntegratedServer server)
    {
        if (this.localIpAddr == null)
        {
            this.localIpAddr = getLocalIpAddr();
        }

        this.setOpenToLan(true);
        this.setDedicated(false);

        PayloadManager.getInstance().resetPayloads();
        PayloadManager.getInstance().verifyPayloads();
        PayloadManager.getInstance().registerHandlers();

        String ipPortString = "["+this.localIpAddr.getHostName()+"] "+this.localIpAddr.getHostAddress() +":"+ server.getServerPort();
        MaLiLib.logger.info("[{}] OpenToLan server listening for connections on {}", MaLiLibReference.MOD_ID, ipPortString);

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerOpenToLan(server);
            }
        }
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerStopping(MinecraftServer server)
    {
        PayloadManager.getInstance().resetPayloads();

        if (MaLiLibReference.isServer())
        {
            ((ConfigManager) ConfigManager.getInstance()).saveAllConfigs();
        }

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerStopping(server);
            }
        }

        // Unregister any handlers at server shutdown
        PayloadManager.getInstance().unregisterHandlers();
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onServerStopped(MinecraftServer server)
    {
        this.setDedicated(false);
        this.setOpenToLan(false);
        this.localIpAddr = null;

        if (!this.handlers.isEmpty())
        {
            for (IServerListener handler : this.handlers)
            {
                handler.onServerStopped(server);
            }
        }
    }
}
