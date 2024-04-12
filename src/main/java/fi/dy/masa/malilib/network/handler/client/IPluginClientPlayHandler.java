package fi.dy.masa.malilib.network.handler.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malilib.network.payload.MaLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;

public interface IPluginClientPlayHandler<T extends CustomPayload> extends ClientPlayNetworking.PlayPayloadHandler<T>
{
    PayloadType getPayloadType();
    default void reset(PayloadType type) {}
    default void registerPlayPayload(PayloadType type) {}
    default void registerPlayHandler(PayloadType type) {}
    default void unregisterPlayHandler(PayloadType type) {}
    default <P extends CustomPayload> void receiveS2CPlayPayload(PayloadType type, P payload, ClientPlayNetworking.Context ctx) {}
    default <P extends CustomPayload> void receiveS2CPlayPayload(PayloadType type, P payload, ClientPlayNetworkHandler handler, CallbackInfo ci) {}
    default void decodeS2CNbtCompound(PayloadType type, NbtCompound data) {}
    default void decodeS2CByteBuf(PayloadType type, MaLibByteBuf data) {}
    default <P extends CustomPayload> void sendC2SPlayPayload(PayloadType type, P payload) {}
    default <P extends CustomPayload> void sendC2SPlayPayload(PayloadType type, P payload, ClientPlayNetworkHandler handler) {}
    default void encodeC2SNbtCompound(PayloadType type, NbtCompound data) {}
    default void encodeC2SByteBuf(PayloadType type, MaLibByteBuf data) {}
}
