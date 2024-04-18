package fi.dy.masa.malilib.network.server;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import fi.dy.masa.malilib.network.payload.MaLiLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;

/**
 * Interface for ServerPlayHandler, for downstream mods.
 * @param <T> (Payload)
 */
public interface IPluginServerPlayHandler<T extends CustomPayload> extends ServerPlayNetworking.PlayPayloadHandler<T>
{
    PayloadType getPayloadType();
    default void reset(PayloadType type) {}
    default void registerPlayPayload(PayloadType type) {}
    default void registerPlayHandler(PayloadType type) {}
    default void unregisterPlayHandler(PayloadType type) {}
    default void decodeC2SNbtCompound(PayloadType type, NbtCompound data, ServerPlayerEntity player) {}
    default void decodeC2SByteBuf(PayloadType type, MaLiLibByteBuf data, ServerPlayerEntity player) {}

    // For reference, but required in the packet handler's if you want to actually send data
    default void encodeS2CNbtCompound(NbtCompound data, ServerPlayerEntity player) {}
    default void encodeS2CByteBuf(MaLiLibByteBuf data, ServerPlayerEntity player) {}
    default <P extends CustomPayload> void receiveC2SPlayPayload(P payload, ServerPlayNetworking.Context ctx) {}
    default <P extends CustomPayload> void receiveC2SPlayPayload(P payload, ServerPlayNetworkHandler handler, CallbackInfo ci) {}
    default <P extends CustomPayload> void sendS2CPlayPayload(P payload, ServerPlayerEntity player) {}
    default <P extends CustomPayload> void sendS2CPlayPayload(P payload, ServerPlayNetworkHandler handler) {}
}
