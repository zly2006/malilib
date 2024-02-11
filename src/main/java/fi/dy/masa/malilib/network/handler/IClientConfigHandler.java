package fi.dy.masa.malilib.network.handler;

import net.minecraft.network.packet.CustomPayload;

public interface IClientConfigHandler {
    <P extends CustomPayload> void registerClientConfigHandler(IPluginConfigHandler<P> handler);

    <P extends CustomPayload> void unregisterClientConfigHandler(IPluginConfigHandler<P> handler);
}
