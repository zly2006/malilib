package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxLitematicsPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxMetadataPayload;
import fi.dy.masa.malilib.network.payload.channel.ServuxStructuresPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * This is made to "manage" the payload types and do the actual channel registrations via the Fabric Network API (4.0.0+)
 * From here, we Map the payload CODEC and TYPE into a HashMap; for our own reference by the Payloads based on their PacketType.
 * This was done in an attempt to make the remaining functions more abstract, which doesn't seem to work in my own attempts,
 * because the Payload Records themselves need to be declared statically.
 */
public class PayloadTypeRegister
{
    // This is how it looks in the static context per a MOD, which must each include its own Custom Payload Records.
    // --> The send/receive handlers can be made into an interface.
    private static final Map<PayloadType, PayloadCodec> TYPES = new HashMap<>();
    public static Identifier getIdentifier(PayloadType type)
    {
        return TYPES.get(type).getId();
    }
    public static String getKey(PayloadType type)
    {
        return TYPES.get(type).getKey();
    }
    private static boolean typesRegistered = false;
    private static boolean playRegistered = false;
    public static void registerType(PayloadType type, String key, String namespace, String path)
    {
        if (!TYPES.containsKey(type))
        {
            PayloadCodec codec = new PayloadCodec(type, key, namespace, path);
            TYPES.put(type, codec);
            MaLiLib.printDebug("PayloadTypeRegister#registerDefaultType(): Successfully registered new Payload id: {} // {}:{}", codec.getId().hashCode(), codec.getId().getNamespace(), codec.getId().getPath());
        }
    }
    public static void registerTypes(String name)
    {
        if (typesRegistered)
            return;
        MaLiLib.printDebug("PayloadTypeRegister#registerDefaultTypes(): executing.");

        String namespace = name;
        if (namespace.isEmpty())
            namespace = MaLiLibReference.COMMON_NAMESPACE;

        // For Carpet "hello" packet (NbtCompound type)
        registerType(PayloadType.CARPET_HELLO, "hello", "carpet", "hello");
        registerType(PayloadType.SERVUX_LITEMATICS, "litematic_shared_storage", "servux", "litematics");
        registerType(PayloadType.SERVUX_METADATA, "metadata_service", "servux", "metadata");
        registerType(PayloadType.SERVUX_STRUCTURES, "structure_bounding_boxes", "servux", "structures");
        typesRegistered = true;
    }
    public static <T extends CustomPayload> void registerPlayChannel(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec)
    {
        PayloadTypeRegistry.playC2S().register(id, codec);
        PayloadTypeRegistry.playS2C().register(id, codec);
    }
    public static void registerPlayChannels()
    {
        // Don't invoke more than once
        if (playRegistered)
            return;
        MaLiLib.printDebug("PayloadTypeRegister#registerPlayChannels(): registering play channels.");

        registerPlayChannel(CarpetHelloPayload.TYPE, CarpetHelloPayload.CODEC);
        registerPlayChannel(ServuxLitematicsPayload.TYPE, ServuxLitematicsPayload.CODEC);
        registerPlayChannel(ServuxMetadataPayload.TYPE, ServuxMetadataPayload.CODEC);
        registerPlayChannel(ServuxStructuresPayload.TYPE, ServuxStructuresPayload.CODEC);
        playRegistered = true;
    }
}
