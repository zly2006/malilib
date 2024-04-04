package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.client.ClientConfigHandler;
import fi.dy.masa.malilib.network.handler.server.ServerConfigHandler;
import fi.dy.masa.malilib.network.handler.client.ClientPlayHandler;
import fi.dy.masa.malilib.network.handler.server.ServerPlayHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.impl.networking.PayloadTypeRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.*;

/**
 * This is made to "manage" the payload types and do the actual channel registrations via the Fabric Network API (4.0.0+)
 * From here, we Map the payload CODEC and TYPE into a HashMap; for our own reference by the Payloads based on their PacketType.
 * This was done in an attempt to make the remaining functions more abstract.
 */
public class PayloadManager
{
    public static final PayloadManager INSTANCE = new PayloadManager();
    public static PayloadManager getInstance() { return INSTANCE; }
    private final Map<PayloadType, PayloadCodec> TYPES = new HashMap<>();

    public PayloadManager()
    {
        initPayloads();
    }

    public void register(PayloadType type, String key, String namespace, String path)
    {
        if (!TYPES.containsKey(type))
        {
            PayloadCodec codec = new PayloadCodec(type, key, namespace, path);
            TYPES.put(type, codec);
            MaLiLib.printDebug("PayloadManager#register(): registering a new PayloadCodec id: {} // {}:{}", codec.getId().hashCode(), codec.getId().getNamespace(), codec.getId().getPath());

        }
        else
        {
            MaLiLib.logger.warn("PayloadManager#register(): type {} already exists.", type.toString());
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
     * Actual Fabric API Config Channel registration.
     */
    public <T extends CustomPayload> void registerConfigChannel(PayloadType type, CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec)
    {
        PayloadCodec codec = getPayloadCodec(type);

        // Never Attempt to "re-register" a channel or bad things will happen.  Kittens harmed, etc.
        if (codec == null || codec.isConfigRegistered())
            return;
        codec.registerConfigCodec();

        // Checks with Fabric APIs IMPL layer (I don't think they are confident with their code yet)
        if (PayloadTypeRegistryImpl.CONFIGURATION_S2C.get(id) != null || PayloadTypeRegistryImpl.CONFIGURATION_C2S.get(id) != null)
        {
            // This just saved Minecraft from crashing, your welcome.
            MaLiLib.logger.error("registerConfigChannel(): blocked duplicate Configuration Channel registration attempt for: {}.", id.id().toString());
        }
        else
        {
            MaLiLib.printDebug("PayloadManager#registerConfigChannel(): [Fabric-API] registering Configuration C2S Channel: {}", id.id().toString());
            PayloadTypeRegistry.configurationC2S().register(id, packetCodec);
            PayloadTypeRegistry.configurationS2C().register(id, packetCodec);
            // We need to register the channel bi-directionally for it to work.
        }
    }

    /**
     * Abstract method for CustomPayload's to define their PACKET_CODEC value.
     */
    @Nullable
    public PayloadCodec getPayloadCodec(PayloadType type)
    {
        //MaLiLib.printDebug("PayloadManager#getPayloadCodec(): type: {}", type.toString());
        return TYPES.getOrDefault(type, null);
    }

    /**
     * Abstract method for CustomPayload's to define their PACKET_TYPE value, derived from the channel Identifier
     */
    @Nullable
    public Identifier getIdentifier(PayloadType type)
    {
        //MaLiLib.printDebug("PayloadManager#getIdentifier(): type: {}", type.toString());
        return TYPES.getOrDefault(type, null).getId();
    }

    /**
     * The Payload "KEY" field is simply for declaring any special "default" key Values for data if none are known,
     * Such as for example nbt.getString(KEY) -- These are not required, but can prove to be very useful.
     */
    @Nullable
    public String getKey(PayloadType type)
    {
        //MaLiLib.printDebug("PayloadManager#getKey(): type: {}", type.toString());
        return TYPES.getOrDefault(type, null).getKey();
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
        // Not found
        return null;
    }

    /**
     * The init for this method.
     * This must be called at the first possible moment, so it can behave like its static.
     * Register the play/config channel codec for every existing PayLoad in our TYPES HashMap<>,
     * or fail to register a channel, then don't attempt to handle the packets using Fabric API.
     * If you try, prepare for Minecraft to crash.
     * Consider yourself to have been warned.
     */
    public void initPayloads()
    {
        //MaLiLib.printDebug("PayloadManager#initPayloads(): invoked.");

        // Do not register Carpet Hello, unless for debugging purposes.
        //register(PayloadType.CARPET_HELLO,      "carpet_hello",             "carpet",   "hello");

        // TODO Uncomment these to enable channel registration, or to create new Payloads
        //register(PayloadType.MALILIB_BYTEBUF,   "malilib_bytebuf",          "malilib",  "bytebuf");
        //register(PayloadType.SERVUX_BLOCKS,     "block_metadata",           "servux",   "blocks");
        //register(PayloadType.SERVUX_ENTITIES,   "entity_provider",          "servux",   "entities");
        //register(PayloadType.SERVUX_LITEMATICS, "litematic_shared_storage", "servux",   "litematics");
        //register(PayloadType.SERVUX_METADATA,   "metadata_service",         "servux",   "metadata");
        register(PayloadType.SERVUX_STRUCTURES, "structure_bounding_boxes", "servux",   "structures");

        // Debugging call
        //listTypes();
    }

    /**
     * Forces a reset() signal on all registered payloads
     */
    public void resetPayloads()
    {
        MaLiLib.printDebug("PayloadManager#resetPayloads(): sending reset() to all registered Payload listeners.");
        //listTypes();

        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#resetPayloads(): Play Client Reset for type {}", type.toString());
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).reset(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#resetPayloads(): Play Server Reset for type {}", type.toString());
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).reset(type);
                }
            }
            if (TYPES.get(type).isConfigRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#resetPayloads(): Config Client Reset for type {}", type.toString());
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).reset(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#resetPayloads(): Config Server Reset for type {}", type.toString());
                    ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).reset(type);
                }
            }
        }
    }

    public void verifyAllPayloads()
    {
        MaLiLib.printDebug("PayloadManager#verifyAllPayloads(): sending registerPayloads() to all registered Payload listeners.");
        //listTypes();

        for (PayloadType type : TYPES.keySet())
        {
            if (!TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#verifyAllPayloads(): Play Client Payloads for type {}", type.toString());
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayPayload(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#verifyAllPayloads(): Play Server Payloads for type {}", type.toString());
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).registerPlayPayload(type);
                }
            }
            if (!TYPES.get(type).isConfigRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#verifyAllPayloads(): Config Client Payloads for type {}", type.toString());
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).registerConfigPayload(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#verifyAllPayloads(): Config Server Payloads for type {}", type.toString());
                    ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).registerConfigPayload(type);
                }
            }
        }
    }

    /**
     * Forces a Type Handler Registration signal on all registered payloads
     */
    public void registerAllHandlers()
    {
        MaLiLib.printDebug("PayloadManager#registerAllHandlers(): sending registerHandlers() to all registered Payload listeners.");
        //listTypes();

        for (PayloadType type : TYPES.keySet())
        {
            if (TYPES.get(type).isPlayRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#registerAllHandlers(): Play Client Handlers for type {}", type.toString());
                    ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).registerPlayHandler(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#registerAllHandlers(): Play Server Handlers for type {}", type.toString());
                    ((ServerPlayHandler<?>) ServerPlayHandler.getInstance()).registerPlayHandler(type);
                }
            }
            if (TYPES.get(type).isConfigRegistered())
            {
                if (MaLiLibReference.isClient())
                {
                    //MaLiLib.printDebug("PayloadManager#registerAllHandlers(): Config Client Handlers for type {}", type.toString());
                    ((ClientConfigHandler<?>) ClientConfigHandler.getInstance()).registerConfigHandler(type);
                }
                if (MaLiLibReference.isServer() || MaLiLibReference.isOpenToLan() || MaLiLibReference.isDedicated())
                {
                    //MaLiLib.printDebug("PayloadManager#registerAllHandlers(): Config Server Handlers for type {}", type.toString());
                    ((ServerConfigHandler<?>) ServerConfigHandler.getInstance()).registerConfigHandler(type);
                }
            }
        }
    }

    // For Debugging only
    public void listTypes()
    {
        for (PayloadCodec codec : TYPES.values())
        {
            MaLiLib.printDebug("listTypes(): type {} // {}", codec.getType().toString(), codec.getId().toString());
        }
    }
}
