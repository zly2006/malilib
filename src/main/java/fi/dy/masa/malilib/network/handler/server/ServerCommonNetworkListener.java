package fi.dy.masa.malilib.network.handler.server;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.IServerCommonNetworkBase;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import fi.dy.masa.malilib.network.payload.channel.*;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * These only exist to handle the Mixin-based "onCustomPayload()" packets, so we can
 * accept non-Fabric API data under a unified interface, and to keep the actual Mixin clean.
 * The Query, Login, Handshake, and Cookie Packet types are not implemented by ServerCommonNetworkHandler,
 * and are really of no use to us.
 */
public class ServerCommonNetworkListener implements IServerCommonNetworkBase
{
    private static final ServerCommonNetworkListener INSTANCE = new ServerCommonNetworkListener();
    public static ServerCommonNetworkListener getInstance() { return INSTANCE; }

    @Override
    public <H extends ServerCommonNetworkHandler> void handleServerPayload(H handler, CustomPayloadC2SPacket packet, MinecraftServer server, CallbackInfo ci)
    {
        CustomPayload thisPayload = packet.payload();
        Identifier id = thisPayload.getId().id();
        PayloadType type = PayloadManager.getInstance().getPayloadType(id);

        if (type == null)
        {
            return;
        }

        if (handler instanceof ServerConfigurationNetworkHandler configHandler)
        {
            MaLiLib.printDebug("ServerCommonNetworkListener(): [CONFIG] received packet of type {} via networkHandler [{}]", type, id.toString());

            switch (type)
            {
                // TODO --> Entries need to exist here for every MaLiLib type Payload
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) thisPayload;

                    NetworkThreadUtils.forceMainThread(packet, configHandler, server);
                    ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, configHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleServerPayload(): [CONFIG] unhandled packet received of type: {} // {}", type, thisPayload.getId().id());
                    break;
            }
        }
        else if (handler instanceof ServerPlayNetworkHandler playHandler)
        {
            MaLiLib.printDebug("ServerCommonNetworkListener(): [PLAY] received packet of type {} via networkHandler [{}]", type, id.toString());

            switch (type)
            {
                // TODO --> Entries need to exist here for every MaLiLib type Payload
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) thisPayload;

                    NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, playHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleServerPayload(): [PLAY] unhandled packet received of type: {} // {}", type, thisPayload.getId().id());
                    break;
            }
        }
    }
}
