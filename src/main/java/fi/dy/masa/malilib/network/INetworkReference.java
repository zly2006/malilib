package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLibReference;

import java.net.InetAddress;

public interface INetworkReference
{
    InetAddress getLocalIpAddr();
    boolean isDedicated();
    boolean isIntegrated();
    boolean isOpenToLan();
    void setLocalIpAddr(InetAddress addr);
    void setDedicated(boolean toggle);
    void setIntegrated(boolean toggle);
    void setOpenToLan(boolean toggle);

    default boolean isClient() { return MaLiLibReference.isClient(); }
    default boolean isServer() { return MaLiLibReference.isServer(); }
}
