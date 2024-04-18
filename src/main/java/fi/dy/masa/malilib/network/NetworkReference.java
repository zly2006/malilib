package fi.dy.masa.malilib.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Used for referencing the Minecraft Run Status to manage the split between Client and Server API
 */
public class NetworkReference implements INetworkReference
{
    private static final NetworkReference INSTANCE = new NetworkReference();
    private static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isServer() { return MOD_ENVIRONMENT == EnvType.SERVER; }

    private boolean dedicated_server = false;
    private boolean integrated = false;
    private boolean open_to_lan = false;
    private InetAddress localIpAddr = null;

    public static NetworkReference getInstance() { return INSTANCE; }

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
    public boolean isIntegrated() { return this.integrated; }

    @Override
    public boolean isOpenToLan() { return this.open_to_lan; }

    @Override
    public void setLocalIpAddr(InetAddress addr)
    {
        this.localIpAddr = addr;
    }

    @Override
    public void setDedicated(boolean toggle)
    {
        if (toggle && isServer())
        {
            this.dedicated_server = true;
            this.open_to_lan = false;
            this.integrated = false;
        }
        else
        {
            this.dedicated_server = false;
        }
    }

    @Override
    public void setIntegrated(boolean toggle)
    {
        if (toggle && isClient())
        {
            this.integrated = true;
            this.dedicated_server = false;
        }
        else
        {
            this.integrated = false;
        }
    }

    @Override
    public void setOpenToLan(boolean toggle)
    {
        if (toggle && isClient())
        {
            this.open_to_lan = true;
            this.integrated = true;
            this.dedicated_server = false;
        }
        else
        {
            this.open_to_lan = false;
        }
    }
}
