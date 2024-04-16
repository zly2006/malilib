package fi.dy.masa.malilib.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import fi.dy.masa.malilib.MaLiLibReference;

public class NetworkReference implements INetworkReference
{
    private static final NetworkReference INSTANCE = new NetworkReference();
    private boolean dedicated_server = false;
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
    public boolean isOpenToLan() { return this.open_to_lan; }

    @Override
    public void setLocalIpAddr(InetAddress addr)
    {
        this.localIpAddr = addr;
    }

    @Override
    public void setDedicated(boolean toggle)
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

    @Override
    public void setOpenToLan(boolean toggle)
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
}
