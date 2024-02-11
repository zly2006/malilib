package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.network.payload.MaLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;

public interface IPluginPlayHandler<T extends CustomPayload> extends ClientPlayNetworking.PlayPayloadHandler<T>
{
    PayloadType getPayloadType();
    default void reset(PayloadType type) {}
    default void registerPlayPayload(PayloadType type) {}
    default void registerPlayHandler(PayloadType type) {}
    default void unregisterPlayHandler(PayloadType type) {}
    default <P extends CustomPayload> void receiveS2CPlayPayload(PayloadType type, P payload, ClientPlayNetworking.Context ctx) { }
    default void decodeS2CNbtCompound(PayloadType type, NbtCompound data) {}
    default void decodeS2CByteBuf(PayloadType type, MaLibByteBuf data) {}

    // Sender/Encoders need to be implemented on the Mod end, so we provide them with an interface for calling ClientPlay.
    default <P extends CustomPayload> void sendC2SPlayPayload(PayloadType type, P payload) {}
    default void encodeC2SNbtCompound(PayloadType type, NbtCompound data) {}
    default void encodeC2SByteBuf(PayloadType type, MaLibByteBuf data) {}

    /*
    TODO -- Cleanup Legacy code
    Identifier getChannel();

    default PlayChannelHandler getClientPacketHandler()
    {
        if (this.usePacketSplitter())
        {
            return (mc, net, buf, responder) -> this.handleViaPacketSplitter(net, buf);
        }

        return (mc, net, buf, responder) -> MinecraftClient.getInstance().execute(() -> this.onPacketReceived(buf));
    }
    default void handleViaPacketSplitter(ClientPlayPacketListener netHandler, PacketByteBuf buf)
    {
        PacketByteBuf fullBuf = PacketSplitter.receive(netHandler, this.getChannel(), buf);

        if (fullBuf != null)
        {
            MinecraftClient.getInstance().execute(() -> this.onPacketReceived(fullBuf));
        }
    }

    //void onPacketReceived(PacketByteBuf buf);

    default boolean usePacketSplitter()
    {
        return true;
    }
     */
}
