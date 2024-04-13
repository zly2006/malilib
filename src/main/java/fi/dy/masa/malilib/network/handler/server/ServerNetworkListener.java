package fi.dy.masa.malilib.network.handler.server;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.channel.ServuxStructuresPayload;

/**
 * These only exist to handle the Mixin-based "onCustomPayload()" packets, so we can
 * accept non-Fabric API data under a unified interface, and to keep the actual Mixin clean.
 * The Query, Login, Handshake, and Cookie Packet types are not implemented by ServerCommonNetworkHandler,
 * and are really of no use to us.
 */
public class ServerNetworkListener implements IServerNetworkBase
{
    private static final ServerNetworkListener INSTANCE = new ServerNetworkListener();
    public static ServerNetworkListener getInstance() { return INSTANCE; }

    @Override
    public <H extends ServerCommonNetworkHandler> void handleServerPayload(H handler, CustomPayloadC2SPacket packet, @Nullable MinecraftServer server, CallbackInfo ci)
    {
        CustomPayload thisPayload = packet.payload();
        Identifier id = thisPayload.getId().id();
        PayloadType type = PayloadManager.getInstance().getPayloadType(id);

        if (type == null)
        {
            return;
        }

        if (handler instanceof ServerPlayNetworkHandler playHandler)
        {
            //MaLiLib.printDebug("ServerNetworkListener(): [PLAY] received packet of type {} via networkHandler [{}]", type, id.toString());

            switch (type)
            {
                // TODO --> Entries need to exist here for every MaLiLib type Payload
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) thisPayload;

                    NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, playHandler, ci);
                    break;
                default:
                    MaLiLib.logger.warn("handleServerPayload(): [PLAY] unhandled packet received of type: {} // {}", type, thisPayload.getId().id());
                    break;
            }
        }
    }
}
