package fi.dy.masa.malilib.network.legacy;

public interface IClientPacketChannelHandler
{
    void registerClientChannelHandler(IPluginChannelHandler handler);

    void unregisterClientChannelHandler(IPluginChannelHandler handler);
}
