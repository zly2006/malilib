package fi.dy.masa.malilib.mixin;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.config.ServerConfigHandler;
import fi.dy.masa.malilib.network.handler.play.ServerPlayHandler;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.payload.channel.*;

import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public class MixinServerCommonNetworkHandler
{
    @Shadow @Final protected MinecraftServer server;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void malilib_onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci)
    {
        MaLiLib.printDebug("MixinServerCommonNetworkHandler#servux_onCustomPayload(): invoked");
        Object thisObj = this;
        if (thisObj instanceof ServerPlayNetworkHandler playHandler)
        {
            CustomPayload thisPayload = packet.payload();
            PayloadType type = PayloadTypeRegister.getInstance().getPayloadType(thisPayload.getId().id());

            if (type != null)
            {
                switch (type)
                {
                    case SERVUX_BLOCKS:
                        ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, playHandler, ci);
                        break;
                    case MALILIB_BYTEBUF:
                        MaLibBufPayload servuxPayload = (MaLibBufPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.MALILIB_BYTEBUF, servuxPayload, playHandler, ci);
                        break;
                    case SERVUX_ENTITIES:
                        ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, playHandler, ci);
                        break;
                    case SERVUX_LITEMATICS:
                        ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, playHandler, ci);
                        break;
                    case SERVUX_METADATA:
                        ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_METADATA, metadataPayload, playHandler, ci);
                        break;
                    case SERVUX_STRUCTURES:
                        ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, playHandler, playHandler.player.getServerWorld());
                        ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).receiveC2SPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, playHandler, ci);
                        break;
                    default:
                        MaLiLib.logger.error("servux_onCustomPayload(): [PLAY] unhandled packet received of type: {} // {}", type, thisPayload.getId().id());
                        break;
                }

                // According to PacketTypeRegister, we own this, so cancel it.
                if (ci.isCancellable())
                    ci.cancel();
            }
        }
        else if (thisObj instanceof ServerConfigurationNetworkHandler configHandler)
        {
            CustomPayload thisPayload = packet.payload();
            PayloadType type = PayloadTypeRegister.getInstance().getPayloadType(thisPayload.getId().id());

            if (type != null)
            {
                switch (type)
                {
                    case SERVUX_BLOCKS:
                        ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, configHandler, ci);
                        break;
                    case MALILIB_BYTEBUF:
                        MaLibBufPayload servuxPayload = (MaLibBufPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.MALILIB_BYTEBUF, servuxPayload, configHandler, ci);
                        break;
                    case SERVUX_ENTITIES:
                        ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, configHandler, ci);
                        break;
                    case SERVUX_LITEMATICS:
                        ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, configHandler, ci);
                        break;
                    case SERVUX_METADATA:
                        ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_METADATA, metadataPayload, configHandler, ci);
                        break;
                    case SERVUX_STRUCTURES:
                        ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) thisPayload;
                        NetworkThreadUtils.forceMainThread(packet, configHandler, this.server);
                        ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).receiveC2SConfigPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, configHandler, ci);
                        break;
                    default:
                        MaLiLib.logger.error("servux_onCustomPayload(): [CONFIG] unhandled packet received of type: {} // {}", type, thisPayload.getId().id());
                        break;
                }

                // According to PacketTypeRegister, we own this, so cancel it.
                if (ci.isCancellable())
                    ci.cancel();
            }
        }
    }
}
