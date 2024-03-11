package fi.dy.masa.malilib.listeners;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import java.net.*;

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
            MaLiLibReference.setIntegrated(true);
            MaLiLibReference.setOpenToLan(false);
            MaLiLibReference.setDedicated(false);
            MaLiLib.printDebug("[{}] Single Player/Integrated Server Mode detected", MaLiLibReference.MOD_ID);
        }
        else if (server.isDedicated())
        {
            MaLiLibReference.setDedicated(true);
            MaLiLib.printDebug("[{}] Dedicated Server Mode detected", MaLiLibReference.MOD_ID);
        }

        //PacketListenerRegister.registerListeners();

        if (!MaLiLibReference.isClient())
        {
            ((ConfigManager) ConfigManager.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onServerStarted(MinecraftServer server)
    {
        PayloadTypeRegister.getInstance().verifyAllPayloads();
        PayloadTypeRegister.getInstance().registerAllHandlers();

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
        MaLiLib.printDebug("[{}] Integrated Server Mode detected", MaLiLibReference.MOD_ID);
        MaLiLibReference.setIntegrated(true);
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
        MaLiLibReference.setIntegrated(true);
        MaLiLibReference.setOpenToLan(true);
        MaLiLibReference.setDedicated(false);

        // This is to register all Server-Side Network API for OpenToLan functionality
        PayloadTypeRegister.getInstance().resetPayloads();
        PayloadTypeRegister.getInstance().verifyAllPayloads();
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
