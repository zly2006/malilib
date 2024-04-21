package fi.dy.masa.malilib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

/**
 * Interface for ClientPlayHandler, for downstream mods.
 * @param <T> (Payload)
 */
public interface IPluginClientPlayHandler<T extends CustomPayload> extends ClientPlayNetworking.PlayPayloadHandler<T>
{
    /**
     * Returns your HANDLER's CHANNEL ID
     * @return (Channel ID)
     */
    Identifier getPayloadChannel();

    /**
     * Returns if your Channel ID has been registered to your Play Payload.
     * @param channel (Your Channel ID)
     * @return (true / false)
     */
    boolean isPlayRegistered(Identifier channel);

    /**
     * Sets your HANDLER as registered.
     * @param channel (Your Channel ID)
     */
    default void setPlayRegistered(Identifier channel) {}

    /**
     * Send your HANDLER a global reset() event, such as when the client is shutting down, or logging out.
     * @param channel (Your Channel ID)
     */
    default void reset(Identifier channel) {}

    /**
     * Register your Payload with Fabric API.
     * This is called immediately upon HANDLER registration.
     * See the fabric-networking-api-v1 Java Docs under PayloadTypeRegistry -> register()
     * for more information on how to do this.
     * -
     * @param channel (Your Channel ID)
     */
    default void registerPlayPayload(Identifier channel) {}

    /**
     * Register your Packet Receiver function.
     * You can use the HANDLER itself (Singleton method), or any other class that you choose.
     * See the fabric-network-api-v1 Java Docs under ClientPlayNetworking.registerGlobalReceiver()
     * for more information on how to do this.
     * -
     * @param channel (Your Channel ID)
     */
    default void registerPlayHandler(Identifier channel) {}

    /**
     * Unregisters your Packet Receiver function.
     * You can use the HANDLER itself (Singleton method), or any other class that you choose.
     * See the fabric-network-api-v1 Java Docs under ClientPlayNetworking.unregisterGlobalReceiver()
     * for more information on how to do this.
     * -
     * @param channel (Your Channel ID)
     */
    default void unregisterPlayHandler(Identifier channel) {}

    /**
     * Receive Payload by pointing static receive() method to this to convert Payload to its data decode() function.
     * -
     * @param payload (Payload to decode)
     * @param ctx (Fabric Context)
     * @param <P> (Payload Param)
     */
    default <P extends CustomPayload> void receivePlayPayload(P payload, ClientPlayNetworking.Context ctx) {}

    /**
     * Receive Payload via the legacy "onCustomPayload" from a Network Handler Mixin interface.
     * -
     * @param payload (Payload to decode)
     * @param handler (Network Handler that received the data)
     * @param ci (Callbackinfo for sending ci.cancel(), if wanted)
     * @param <P> (Payload Param)
     */
    default <P extends CustomPayload> void receivePlayPayload(P payload, ClientPlayNetworkHandler handler, CallbackInfo ci) {}

    /**
     * Payload Decoder wrapper function.
     * Implements how the data is processed after being decoded from the receivePayload().
     * You can ignore these and implement your own helper class/methods.
     * These are provided as an example, and can be used in your HANDLER directly.
     * -
     * @param channel (Channel)
     * @param data (Data Codec)
     */
    default void decodeNbtCompound(Identifier channel, NbtCompound data) {}
    default void decodeByteBuf(Identifier channel, MaLiLibBuf data) {}
    default void decodeObjects(Identifier channel, Object... args) {}

    /**
     * Payload Encoder wrapper function.
     * Implements how to encode() your Payload, then forward complete Payload to sendPayload().
     * -
     * @param data (Data Codec)
     */
    default void encodeNbtCompound(NbtCompound data) {}
    default void encodeByteBuf(MaLiLibBuf data) {}
    default void encodeObject(Object... args) {}

    /**
     * Sends the Payload to the server using the Fabric-API interface.
     * -
     * @param payload (The Payload to send)
     * @param <P> (Payload Param)
     */
    default <P extends CustomPayload> void sendPlayPayload(P payload)
    {
        if (payload.getId().id().equals(this.getPayloadChannel()) && this.isPlayRegistered(this.getPayloadChannel()) &&
            ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
        }
    }

    /**
     * Sends the Payload to the player using the ClientPlayNetworkHandler interface.
     * @param handler (ServerPlayNetworkHandler)
     * @param payload (The Payload to send)
     * @param <P> (Payload Param)
     */
    default <P extends CustomPayload> void sendPlayPayload(ClientPlayNetworkHandler handler, P payload)
    {
        if (payload.getId().id().equals(this.getPayloadChannel()) && this.isPlayRegistered(this.getPayloadChannel()))
        {
            Packet<?> packet = new CustomPayloadC2SPacket(payload);

            if (handler != null && handler.accepts(packet))
            {
                handler.sendPacket(packet);
            }
        }
    }
}
