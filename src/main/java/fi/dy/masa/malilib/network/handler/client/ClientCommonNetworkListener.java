package fi.dy.masa.malilib.network.handler.client;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.IClientCommonNetworkBase;
import fi.dy.masa.malilib.network.packet_example.PacketType;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.payload.channel.*;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
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
        PayloadType type = PayloadTypeRegister.getInstance().getPayloadType(id);

        if (type == null)
        {
            return;
        }

        if (handler instanceof ClientConfigurationNetworkHandler configHandler)
        {
            switch (type)
            {
                case CARPET_HELLO:
                    // Don't handle Carpet packets if we have Carpet-Client installed
                    if (MaLiLibReference.hasCarpetClient())
                    {
                        ci = new CallbackInfo(ci.getId(), false);

                        // Create a Fake Carpet Packet
                        NbtCompound nbt = new NbtCompound();
                        nbt.putString(PacketType.CarpetHello.HI, MaLiLibReference.MOD_STRING);
                        CarpetHelloPayload fakeCarpetPayload = new CarpetHelloPayload(nbt);
                        //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                        ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.CARPET_HELLO, fakeCarpetPayload, configHandler, ci);
                    }
                    else
                    {
                        CarpetHelloPayload realCarpetPayload = (CarpetHelloPayload) packet;
                        //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                        ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.CARPET_HELLO, realCarpetPayload, configHandler, ci);
                    }
                    break;
                case MALILIB_BYTEBUF:
                    MaLibBufPayload malilibPayload = (MaLibBufPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.MALILIB_BYTEBUF, malilibPayload, configHandler, ci);
                    break;
                case SERVUX_BLOCKS:
                    ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, configHandler, ci);
                    break;
                case SERVUX_ENTITIES:
                    ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, configHandler, ci);
                    break;
                case SERVUX_LITEMATICS:
                    ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, configHandler, ci);
                    break;
                case SERVUX_METADATA:
                    ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_METADATA, metadataPayload, configHandler, ci);
                    break;
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, configHandler, mc);
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, configHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleClientPayload(): [CONFIG] unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }
        }
        else if (handler instanceof ClientPlayNetworkHandler playHandler)
        {
            switch (type)
            {
                case CARPET_HELLO:
                    // Don't handle Carpet packets if we have Carpet-Client installed
                    if (MaLiLibReference.hasCarpetClient())
                    {
                        ci = new CallbackInfo(ci.getId(), false);
                        // Create a Fake Carpet Packet
                        NbtCompound nbt = new NbtCompound();
                        nbt.putString(PacketType.CarpetHello.HI, MaLiLibReference.MOD_STRING);
                        CarpetHelloPayload fakeCarpetPayload = new CarpetHelloPayload(nbt);

                        //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                        ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.CARPET_HELLO, fakeCarpetPayload, playHandler, ci);
                    }
                    else
                    {
                        CarpetHelloPayload realCarpetPayload = (CarpetHelloPayload) packet;
                        //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                        ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.CARPET_HELLO, realCarpetPayload, playHandler, ci);
                    }
                    break;
                case MALILIB_BYTEBUF:
                    MaLibBufPayload malilibPayload = (MaLibBufPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.MALILIB_BYTEBUF, malilibPayload, playHandler, ci);
                    break;
                case SERVUX_BLOCKS:
                    ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, playHandler, ci);
                    break;
                case SERVUX_ENTITIES:
                    ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, playHandler, ci);
                    break;
                case SERVUX_LITEMATICS:
                    ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, playHandler, ci);
                    break;
                case SERVUX_METADATA:
                    ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_METADATA, metadataPayload, playHandler, ci);
                    break;
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;
                    //NetworkThreadUtils.forceMainThread(packet, playHandler, mc);
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, playHandler, ci);
                    break;
                default:
                    MaLiLib.logger.error("handleClientPayload(): [PLAY] unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }
        }
    }
}
