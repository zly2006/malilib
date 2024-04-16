package fi.dy.masa.malilib.network;

import java.net.InetAddress;

public interface INetworkReference
{
    InetAddress getLocalIpAddr();
    boolean isDedicated();
    boolean isOpenToLan();
    void setLocalIpAddr(InetAddress addr);
    void setDedicated(boolean toggle);
    void setOpenToLan(boolean toggle);
}
