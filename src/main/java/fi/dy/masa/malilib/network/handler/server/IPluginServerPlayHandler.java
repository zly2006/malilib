package fi.dy.masa.malilib.network.handler.server;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import fi.dy.masa.malilib.network.payload.MaLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;

public interface IPluginServerPlayHandler<T extends CustomPayload> extends ServerPlayNetworking.PlayPayloadHandler<T>
{
    PayloadType getPayloadType();
    default void reset(PayloadType type) {}
    default void registerPlayPayload(PayloadType type) {}
    default void registerPlayHandler(PayloadType type) {}
    default void unregisterPlayHandler(PayloadType type) {}
    default <P extends CustomPayload> void receiveC2SPlayPayload(PayloadType type, P payload, ServerPlayNetworking.Context ctx) {}
    default <P extends CustomPayload> void receiveC2SPlayPayload(PayloadType type, P payload, ServerPlayNetworkHandler handler, CallbackInfo ci) {}
    default void decodeC2SNbtCompound(PayloadType type, NbtCompound data, ServerPlayerEntity player) {}
    default void decodeC2SByteBuf(PayloadType type, MaLibByteBuf data, ServerPlayerEntity player) {}
    default <P extends CustomPayload> void sendS2CPlayPayload(PayloadType type, P payload, ServerPlayerEntity player) {}
    default <P extends CustomPayload> void sendS2CPlayPayload(PayloadType type, P payload, ServerPlayNetworkHandler handler) {}
    default void encodeS2CNbtCompound(PayloadType type, NbtCompound data, ServerPlayerEntity player) {}
    default void encodeS2CByteBuf(PayloadType type, MaLibByteBuf data, ServerPlayerEntity player) {}
}
