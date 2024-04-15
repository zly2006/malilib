package fi.dy.masa.malilib.event;

import java.net.InetAddress;
import fi.dy.masa.malilib.interfaces.IServerListener;

public interface IServerManager
{
    void registerServerHandler(IServerListener handler);
    void unregisterServerHandler(IServerListener handler);

    InetAddress getLocalIpAddr();
    boolean isDedicated();
    boolean isOpenToLan();
}
