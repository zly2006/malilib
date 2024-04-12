package fi.dy.masa.malilib.network.handler.client;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.IClientCommonNetworkBase;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import fi.dy.masa.malilib.network.payload.channel.*;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * These only exist to handle the Mixin-based "onCustomPayload()" packets, so we can
 * accept non-Fabric API data under a unified interface, and to keep the actual Mixin clean.
 * The Query, Login, Handshake, and Cookie Packet types are not implemented by ServerCommonNetworkHandler,
 * and are really of no use to us.
 */
public class ClientCommonNetworkListener implements IClientCommonNetworkBase
{
    private static final ClientCommonNetworkListener INSTANCE = new ClientCommonNetworkListener();
    public static ClientCommonNetworkListener getInstance() { return INSTANCE; }

    @Override
    public <H extends ClientCommonNetworkHandler> void handleClientPayload(H handler, CustomPayload packet, CallbackInfo ci)
    {
        //CustomPayload thisPayload = packet.payload();
        Identifier id = packet.getId().id();
        PayloadType type = PayloadManager.getInstance().getPayloadType(id);

        if (type == null)
        {
            return;
        }

        if (handler instanceof ClientConfigurationNetworkHandler configHandler)
        {
            MaLiLib.printDebug("ClientCommonNetworkListener(): [CONFIG] received packet of type {} via networkHandler [{}]", type, id.toString());

            switch (type)
            {
                // TODO --> Entries need to exist here for every MaLiLib type Payload
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;

                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, configHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleClientPayload(): [CONFIG] unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }
        }
        else if (handler instanceof ClientPlayNetworkHandler playHandler)
        {
            MaLiLib.printDebug("ClientCommonNetworkListener(): [PLAY] received packet of type {} via networkHandler [{}]", type, id.toString());

            switch (type)
            {
                // TODO --> Entries need to exist here for every MaLiLib type Payload
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;

                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, playHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleClientPayload(): [PLAY] unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }
        }
    }
}
