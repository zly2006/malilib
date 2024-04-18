package fi.dy.masa.malilib.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadManager;

/**
 * This is used on downstream mods, such as MiniHUD and ServuX, and this manages NetworkReference.
 * This is critical for the Network API to function properly at the correct timings,
 * and to help manage ModInitTasks in a Server Environment versus the Client Environment.
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
            NetworkReference.getInstance().getLocalIpAddr();
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

        NetworkReference.getInstance().getLocalIpAddr();
    }

    @Override
    public void onServerStopping(MinecraftServer server)
    {
        PayloadManager.getInstance().resetPayloads();
    }

    @Override
    public void onServerStopped(MinecraftServer server)
    {
        PayloadManager.getInstance().unregisterHandlers();

        NetworkReference.getInstance().setDedicated(false);
        NetworkReference.getInstance().setOpenToLan(false);
        NetworkReference.getInstance().setIntegrated(false);
        NetworkReference.getInstance().setLocalIpAddr(null);
    }
}