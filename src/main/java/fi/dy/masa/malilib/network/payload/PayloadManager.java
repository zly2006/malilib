package fi.dy.masa.malilib.network.payload;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.impl.networking.PayloadTypeRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.event.ServerHandler;
import fi.dy.masa.malilib.network.handler.client.ClientPlayHandler;
import fi.dy.masa.malilib.network.handler.server.ServerPlayHandler;

/**
 * This is made to "manage" the payload types and do the actual channel registrations via the Fabric Network API (4.0.0+)
 * From here, we Map the payload CODEC and TYPE into a HashMap; for our own reference by the Payloads based on their PayloadType.
 * This was done in an attempt to make the remaining functions more abstract.
 */
public class PayloadManager
{
    private static final PayloadManager INSTANCE = new PayloadManager();
    public static PayloadManager getInstance() { return INSTANCE; }
    private final Map<PayloadType, PayloadCodec> TYPES = new HashMap<>();

    public PayloadManager() {}

    /**
     * Registers a Payload Type with PayloadManager
     * -
     * @param type (PayloadType ENUM)
     * @param namespace (Identifier Namespace, ie, servux)
     * @param path (Identifier path, ie, structures)
     */
    public void register(PayloadType type, String namespace, String path)
    {
        if (!TYPES.containsKey(type))
        {
            PayloadCodec codec = new PayloadCodec(type, namespace, path);
            TYPES.put(type, codec);
            MaLiLib.printDebug("PayloadManager#register(): registering a new PayloadCodec id: {} // {}:{}", codec.getId().hashCode(), codec.getId().getNamespace(), codec.getId().getPath());

        }
    }

    /**
     * Actual Fabric API Play Channel registration.
     */
    public <T extends CustomPayload> void registerPlayChannel(PayloadType type, CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec)
    {
        PayloadCodec codec = getPayloadCodec(type);

        // Never Attempt to "re-register" a channel or bad things will happen.  Kittens harmed, etc.
        if (codec == null || codec.isPlayRegistered())
            return;

        codec.registerPlayCodec();

        // Checks with Fabric APIs IMPL layer (I don't think they are confident with their code yet)
        if (PayloadTypeRegistryImpl.PLAY_S2C.get(id) != null || PayloadTypeRegistryImpl.PLAY_C2S.get(id) != null)
        {
            // This just saved Minecraft from crashing, your welcome.
            MaLiLib.logger.error("registerPlayChannel(): blocked duplicate Play Channel registration attempt for: {}.", id.id().toString());
        }
        else
        {
            MaLiLib.printDebug("PayloadManager#registerPlayChannel(): [Fabric-API] registering Play C2S Channel: {}", id.id().toString());
            PayloadTypeRegistry.playC2S().register(id, packetCodec);
            PayloadTypeRegistry.playS2C().register(id, packetCodec);
            // We need to register the channel bi-directionally for it to work.
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
            if (codec != null)
            {
                if (codec.getId().equals(id))
                {
                    return type;
                }
            }
        }

        return null;
    }

    /**
     * Forces a reset() signal on all registered payloads
     */
    public void resetPayloads()
    {
        MaLiLib.printDebug("PayloadManager#resetPayloads(): sending reset() to all Payload listeners.");

        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).reset(type);
                }
                if (MaLiLibReference.isServer() || (ServerHandler.getInstance()).isOpenToLan() || (ServerHandler.getInstance()).isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).reset(type);
                }
            }
        }
    }

    public void verifyPayloads()
    {
        MaLiLib.printDebug("PayloadManager#verifyPayloads(): sending registerPayloads() to all Payload listeners.");

        for (PayloadType type : TYPES.keySet())
        {
            if (!TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayPayload(type);
                }
                if (MaLiLibReference.isServer() || (ServerHandler.getInstance()).isOpenToLan() || (ServerHandler.getInstance()).isDedicated())
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
        MaLiLib.printDebug("PayloadManager#registerHandlers(): sending registerHandlers() to all Payload listeners.");

        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayHandler(type);
                }
                if (MaLiLibReference.isServer() || (ServerHandler.getInstance()).isOpenToLan() || (ServerHandler.getInstance()).isDedicated())
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
        MaLiLib.printDebug("PayloadManager#unregisterHandlers(): sending unregisterHandlers() to all Payload listeners.");

        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).unregisterPlayHandler(type);
                }
                if (MaLiLibReference.isServer() || (ServerHandler.getInstance()).isOpenToLan() || (ServerHandler.getInstance()).isDedicated())
                {
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).unregisterPlayHandler(type);
                }
            }
        }
    }
}
