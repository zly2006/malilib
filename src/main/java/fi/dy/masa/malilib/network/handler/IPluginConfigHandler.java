package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.network.payload.MaLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;

public interface IPluginConfigHandler<T extends CustomPayload> extends ClientConfigurationNetworking.ConfigurationPayloadHandler<T>
{
    PayloadType getPayloadType();
    default void reset(PayloadType type) {}
    default void registerConfigPayload(PayloadType type) {}
    default void registerConfigHandler(PayloadType type) {}
    default void unregisterConfigHandler(PayloadType type) {}
    default <P extends CustomPayload> void receiveS2CConfigPayload(PayloadType type, P payload, ClientConfigurationNetworking.Context ctx) {}
    default void decodeS2CNbtCompound(PayloadType type, NbtCompound data) {}
    default void decodeS2CByteBuf(PayloadType type, MaLibByteBuf data) {}

    // TODO Senders/Encoders need to be implemented on the Mod end,
    //  but we need to provide them with an interface for calling ClientConfig.send on a standard roadmap
    default <P extends CustomPayload> void sendC2SConfigPayload(PayloadType type, P payload) {}
    default void encodeC2SNbtCompound(PayloadType type, NbtCompound data) {}
    default void encodeC2SByteBuf(PayloadType type, MaLibByteBuf data) {}
}
