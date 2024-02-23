package fi.dy.masa.malilib.network.handler.play;

import net.minecraft.network.packet.CustomPayload;

public interface IClientPlayHandler
{
    <P extends CustomPayload> void registerClientPlayHandler(IPluginPlayHandler<P> handler);

    <P extends CustomPayload> void unregisterClientPlayHandler(IPluginPlayHandler<P> handler);
}
