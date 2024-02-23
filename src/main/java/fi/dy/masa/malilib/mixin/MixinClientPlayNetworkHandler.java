package fi.dy.masa.malilib.mixin;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.handler.play.ClientPlayHandler;
import fi.dy.masa.malilib.network.packet.PacketUtils_example;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.payload.channel.*;
import fi.dy.masa.malilib.event.WorldLoadHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

@Mixin(value = ClientPlayNetworkHandler.class, priority = 998)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow
    private ClientWorld world;
    @Unique
    @Nullable
    private ClientWorld worldBefore;

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void malilib_onPreJoinGameHead(GameJoinS2CPacket packet, CallbackInfo ci) {
        // Need to grab the old world reference at the start of the method,
        // because the next injection point is right after the world has been assigned,
        // since we need the new world reference for the callback.
        this.worldBefore = this.world;
    }

    @Inject(method = "onGameJoin", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;joinWorld(" +
                    "Lnet/minecraft/client/world/ClientWorld;)V"))
    private void malilib_onPreGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        // Call only in case channels aren't registered.
        ((WorldLoadHandler) WorldLoadHandler.getInstance()).onWorldLoadPre(this.worldBefore, this.world, MinecraftClient.getInstance());

        // TODO For network API Debugging (For when you join a Remote Server)
        PacketUtils_example.registerPayloads();
        MaLiLib.printDebug("malilib_onPreGameJoin()");
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void malilib_onPostGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ((WorldLoadHandler) WorldLoadHandler.getInstance()).onWorldLoadPost(this.worldBefore, this.world, MinecraftClient.getInstance());
        this.worldBefore = null;

        // For network API handler registration
        PayloadTypeRegister.getInstance().registerAllHandlers();
        MaLiLib.printDebug("malilib_onPostGameJoin()");
    }

    /**
     * OPTIONAL CODE -- NOT REQUIRED!
     * This is for "exposing" Custom Payload Packets that are getting obfuscated behind the Play channel.
     * It also allows for "OpenToLan" functionality to work, because via the Fabric API,
     * the network handlers are set to NULL, and fail to function.
     * If handled this way, you must use ci.cancel() if successfully matched.
     * Perhaps it's a bug in the Fabric API for OpenToLan?
     */
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void malilib_onPlayCustomPayload(CustomPayload packet, CallbackInfo ci)
    {
        /**
         * You can't use packet.getData() here anymore, it no longer exists.
         * You can put this under each Payload Type, though.
         * But to what end if Fabric API can handle this safely?
         */
        if (!MinecraftClient.getInstance().isOnThread())
        {
            return;
        }

        // See if this packet matches one of our registered types
        PayloadType type = PayloadTypeRegister.getInstance().getPayloadType(packet.getId().id());
        if (type != null)
        {
            final ClientPlayNetworkHandler handler = (ClientPlayNetworkHandler) (Object) this;
            switch (type)
            {
                case CARPET_HELLO:
                    CarpetHelloPayload carpetPayload = (CarpetHelloPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.CARPET_HELLO, carpetPayload, handler, ci);
                    break;
                case MALILIB_BYTEBUF:
                    MaLibBufPayload malilibPayload = (MaLibBufPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.MALILIB_BYTEBUF, malilibPayload, handler, ci);
                    break;
                case SERVUX_BLOCKS:
                    ServuxBlocksPayload blocksPayload = (ServuxBlocksPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_BLOCKS, blocksPayload, handler, ci);
                    break;
                case SERVUX_ENTITIES:
                    ServuxEntitiesPayload entitiesPayload = (ServuxEntitiesPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_ENTITIES, entitiesPayload, handler, ci);
                    break;
                case SERVUX_LITEMATICS:
                    ServuxLitematicsPayload litematicsPayload = (ServuxLitematicsPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_LITEMATICS, litematicsPayload, handler, ci);
                    break;
                case SERVUX_METADATA:
                    ServuxMetadataPayload metadataPayload = (ServuxMetadataPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_METADATA, metadataPayload, handler, ci);
                    break;
                case SERVUX_STRUCTURES:
                    ServuxStructuresPayload structuresPayload = (ServuxStructuresPayload) packet;
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).receiveS2CPlayPayload(PayloadType.SERVUX_STRUCTURES, structuresPayload, handler, ci);
                    break;
                default:
                    MaLiLib.logger.error("malilib_onPlayCustomPayload(): unhandled packet received of type: {} // {}", type, packet.getId().id());
                    break;
            }

            // According to PacketTypeRegister, we own this, so cancel it.
            if (ci.isCancellable())
                ci.cancel();
        }
    }
}