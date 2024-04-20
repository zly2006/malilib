package fi.dy.masa.malilib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Interface for ClientPlayHandler, for downstream mods.
 * @param <T> (Payload)
 */
public interface IPluginClientPlayHandler<T extends CustomPayload> extends ClientPlayNetworking.PlayPayloadHandler<T>
{
    Identifier getPayloadChannel();
    boolean isPlayRegistered(Identifier channel);
    default void reset(Identifier channel) {}
    default void registerPlayPayload(Identifier channel) {}
    default void registerPlayHandler(Identifier channel) {}
    default void unregisterPlayHandler(Identifier channel) {}
    default void decodeNbtCompound(Identifier channel, NbtCompound data) {}
    default void decodeByteBuf(Identifier channel, MaLiLibBuf data) {}
    default void decodeObject(Identifier channel, Object data) {}

    // For reference, but required in the packet handler's if you want to actually send data
    default void encodeNbtCompound(NbtCompound data) {}
    default void encodeByteBuf(MaLiLibBuf data) {}
    default void encodeObject(Object data) {}
    default <P extends CustomPayload> void receivePlayPayload(P payload, ClientPlayNetworking.Context ctx) {}
    default <P extends CustomPayload> void receivePlayPayload(P payload, ClientPlayNetworkHandler handler, CallbackInfo ci) {}
    default <P extends CustomPayload> void sendPlayPayload(P payload) {}
    default <P extends CustomPayload> void sendPlayPayload(P payload, ClientPlayNetworkHandler handler) {}
}
