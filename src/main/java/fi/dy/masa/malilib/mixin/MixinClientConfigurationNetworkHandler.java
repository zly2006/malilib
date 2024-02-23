package fi.dy.masa.malilib.mixin;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.config.ClientConfigHandler;
import fi.dy.masa.malilib.network.packet.PacketType_example;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.payload.channel.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConfigurationNetworkHandler.class, priority = 998)
public class MixinClientConfigurationNetworkHandler
{
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void malilib_onCustomPayload(CustomPayload packet, CallbackInfo ci)
    {
        if (!MinecraftClient.getInstance().isOnThread())
        {
            return;
        }

        // See if this packet matches one of our registered types
        Identifier id = packet.getId().id();
        PayloadType type = PayloadTypeRegister.getInstance().getPayloadType(id);
        MaLiLib.printDebug("malilib_onCustomPayload(): [CLIENT-CONFIG] type: {} // id: {}", type, id.toString());

        if (type != null)
        {
            final ClientConfigurationNetworkHandler handler = (ClientConfigurationNetworkHandler) (Object) this;
            switch (type)
            {
                case CARPET_HELLO:
                    // Don't handle Carpet packets if we have Carpet-Client installed
                    if (MaLiLibReference.hasCarpetClient())
                    {
                        // Create a Fake Carpet Packet
                        NbtCompound nbt = new NbtCompound();
                        nbt.putString(PacketType_example.CarpetHello.HI, MaLiLibReference.MOD_ID+"-"+MaLiLibReference.MOD_TYPE+"-"+MaLiLibReference.MC_VERSION+"-"+MaLiLibReference.MOD_VERSION);
                        CarpetHelloPayload fakeCarpetPayload = new CarpetHelloPayload(nbt);

                        ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.CARPET_HELLO, fakeCarpetPayload, handler, ci);
                    }
                    else
                    {
                        ci = new CallbackInfo(ci.getId(), false);
                        CarpetHelloPayload realCarpetPayload = (CarpetHelloPayload) packet;

                        ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.CARPET_HELLO, realCarpetPayload, handler, ci);
                    }
                    break;
                case MALILIB_BYTEBUF:
                    MaLibBufPayload malilibPayload = (MaLibBufPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.MALILIB_BYTEBUF, malilibPayload, handler, ci);
                    break;
                case SERVUX_BLOCKS:
                    ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, handler, ci);
                    break;
                case SERVUX_ENTITIES:
                    ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, handler, ci);
                    break;
                case SERVUX_LITEMATICS:
                    ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, handler, ci);
                    break;
                case SERVUX_METADATA:
                    ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_METADATA, metadataPayload, handler, ci);
                    break;
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).receiveS2CConfigPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, handler, ci);
                    break;
                default:
                    MaLiLib.logger.error("malilib_onCustomPayload(): [CONFIG] unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }

            // According to PacketTypeRegister, we own this, so cancel it.
            //if (ci.isCancellable())
                //ci.cancel();
        }
    }
}
