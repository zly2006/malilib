package fi.dy.masa.malilib.network.payload;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.NetworkReference;
import fi.dy.masa.malilib.network.client.ClientPlayHandler;
import fi.dy.masa.malilib.network.server.ServerPlayHandler;

/**
 * This is made to "manage" the payload types and do the actual channel registrations via the Fabric Network API (4.0.0+)
 * From here, we Map the payload CODEC and TYPE into a HashMap; for our own reference by the Payloads based on their PayloadType.
 * This was done to make the remaining functions more abstract, and allows them to define their own Channel Identifier for each
 * Payload Record file that is created.
 */
public class PayloadManager
{
    private static final PayloadManager INSTANCE = new PayloadManager();
    public static PayloadManager getInstance() { return INSTANCE; }
    private final Map<PayloadType, PayloadCodec> TYPES = new HashMap<>();

    public PayloadManager() {}

    /**
     * Registers a Payload Type with Payload Manager
     *
     * @param type (PayloadType ENUM)
     * @param id (Identifier Namespace:Path, ie, servux:structures)
     */
    public void register(PayloadType type, Identifier id)
    {
        if (TYPES.containsKey(type) == false && type.exists(type))
        {
            PayloadCodec codec = new PayloadCodec(type, id);
            TYPES.put(type, codec);
        }
    }

    /**
     * Abstract method for CustomPayload's to define their PACKET_CODEC value.
     */
    @Nullable
    public PayloadCodec getPayloadCodec(PayloadType type)
    {
        return TYPES.getOrDefault(type, null);
    }

    /**
     * Abstract method for CustomPayload's to define their PACKET_TYPE value, derived from the channel Identifier
     */
    @Nullable
    public Identifier getIdentifier(PayloadType type)
    {
        return TYPES.getOrDefault(type, null).getId();
    }

    /**
     * Search for a registered Payload type by Identifier
     */
    @Nullable
    public PayloadType getPayloadType(Identifier id)
    {
        for (PayloadType type : TYPES.keySet())
        {
            PayloadCodec codec = TYPES.get(type);

            if (codec != null && codec.getId().equals(id))
            {
                return type;
            }
        }

        return null;
    }

    /**
     * Fabric API Play Channel registration.
     */
    public <T extends CustomPayload> void registerPlayChannel(PayloadType type, CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec)
    {
        PayloadCodec codec = this.getPayloadCodec(type);

        // Never Attempt to "re-register" a channel or bad things will happen.  Kittens harmed, etc.
        if (codec == null || codec.isPlayRegistered())
        {
            return;
        }

        //MaLiLib.logger.info("registerPlayChannel: [Fabric-API] registering Play Channel: {}", id.id().toString());
        codec.registerPlayCodec();
        PayloadTypeRegistry.playC2S().register(id, packetCodec);
        PayloadTypeRegistry.playS2C().register(id, packetCodec);
        // We need to register the channel bi-directionally for it to work correctly.
    }

    public <T extends CustomPayload> void registerPlayHandler(CustomPayload.Id<T> type, ClientPlayNetworking.PlayPayloadHandler<T> handler)
    {
        if (NetworkReference.isClient())
        {
            ClientPlayNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void registerPlayHandler(CustomPayload.Id<T> type, ServerPlayNetworking.PlayPayloadHandler<T> handler)
    {
        if (NetworkReference.isServer() || NetworkReference.getInstance().isDedicated() || NetworkReference.getInstance().isOpenToLan())
        {
            ServerPlayNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void unregisterPlayHandler(CustomPayload.Id<T> type)
    {
        if (NetworkReference.isClient())
        {
            ClientPlayNetworking.unregisterGlobalReceiver(type.id());
        }

        if (NetworkReference.isServer() || NetworkReference.getInstance().isDedicated() || NetworkReference.getInstance().isOpenToLan())
        {
            ServerPlayNetworking.unregisterGlobalReceiver(type.id());
        }
    }

    /**
     * Forces a reset() signal on all registered payloads
     */
    public void resetPayloads()
    {
        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (NetworkReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).reset(type);
                }
                if (NetworkReference.isServer() || NetworkReference.getInstance().isOpenToLan() || NetworkReference.getInstance().isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).reset(type);
                }
            }
        }
    }

    /**
     * Forces a registerPlayPayload() signal on all registered payloads
     */
    public void verifyPayloads()
    {
        for (PayloadType type : TYPES.keySet())
        {
            if (!TYPES.get(type).isPlayRegistered())
            {
                if (NetworkReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayPayload(type);
                }
                if (NetworkReference.isServer() || NetworkReference.getInstance().isOpenToLan() || NetworkReference.getInstance().isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).registerPlayPayload(type);
                }
            }
        }
    }

    /**
     * Forces a Type Handler Registration signal on all registered payloads
     */
    public void registerHandlers()
    {
        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (NetworkReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayHandler(type);
                }
                if (NetworkReference.isServer() || NetworkReference.getInstance().isOpenToLan() || NetworkReference.getInstance().isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).registerPlayHandler(type);
                }
            }
        }
    }

    /**
     * Forces a Type Handler De-Registration signal on all registered payloads
     */
    public void unregisterHandlers()
    {
        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (NetworkReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).unregisterPlayHandler(type);
                }
                if (NetworkReference.isServer() || NetworkReference.getInstance().isOpenToLan() || NetworkReference.getInstance().isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).unregisterPlayHandler(type);
                }
            }
        }
    }
}
