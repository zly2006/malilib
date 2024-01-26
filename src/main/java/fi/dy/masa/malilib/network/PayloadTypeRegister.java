package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.*;

public class PayloadTypeRegister
{
    // This is how it looks in the static context per a MOD, which must each include its own Custom Payload Records.
    // --> The send/receive handlers can be made into an interface.
    //public final int MAX_TOTAL_PER_PACKET_S2C = 1048576;
    //public final int MAX_TOTAL_PER_PACKET_C2S = 32767;
    private static final Map<PayloadType, PayloadCodec> TYPES = new HashMap<>();
    private static boolean channelTypeInit = false;
    private static boolean channelsInit = false;

    public static Identifier getIdentifier(PayloadType type)
    {
        return TYPES.get(type).getId();
    }
    public static String getKey(PayloadType type)
    {
        return TYPES.get(type).getKey();
    }
    public static void registerDefaultType(PayloadType type, String key, String namespace)
    {
        if (!TYPES.containsKey(type))
        {
            PayloadCodec codec = new PayloadCodec(type, key, namespace);
            TYPES.put(type, codec);
            MaLiLib.printDebug("PayloadTypeRegister#registerDefaultType(): Successfully registered new Payload id: {} // {}:{}", codec.getId().hashCode(), codec.getId().getNamespace(), codec.getId().getPath());
        }
    }
    public static void registerType(PayloadType type, String key, String namespace, String path)
    {
        if (!TYPES.containsKey(type))
        {
            PayloadCodec codec = new PayloadCodec(type, key, namespace, path);
            TYPES.put(type, codec);
            MaLiLib.printDebug("PayloadTypeRegister#registerDefaultType(): Successfully registered new Payload id: {} // {}:{}", codec.getId().hashCode(), codec.getId().getNamespace(), codec.getId().getPath());
        }
    }
    public static void registerDefaultTypes(String name)
    {
        // Don't invoke more than once
        if (channelsInit || channelTypeInit)
            return;
        MaLiLib.printDebug("PayloadTypeRegister#registerDefaultTypes(): executing.");

        String namespace = name;
        if (namespace.isEmpty())
            namespace = MaLiLibReference.COMMON_NAMESPACE;

        registerDefaultType(PayloadType.STRING, "string", namespace);
        registerDefaultType(PayloadType.DATA, "data", namespace);
        // For Carpet "hello" packet (NbtCompound type)
        registerType(PayloadType.CARPET_HELLO, "hello", "carpet", "hello");
        registerType(PayloadType.SERVUX, "structure_bounding_boxes", "servux", "structures");
        channelTypeInit = true;
    }
    public static <T extends CustomPayload> void registerDefaultPlayChannel(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec)
    {
        PayloadTypeRegistry.playC2S().register(id, codec);
        PayloadTypeRegistry.playS2C().register(id, codec);
    }
    public static void registerDefaultPlayChannels()
    {
        // Don't invoke more than once
        if (channelsInit)
            return;
        MaLiLib.printDebug("PayloadTypeRegister#registerPlayChannels(): registering play channels.");
        registerDefaultPlayChannel(DataPayload.TYPE, DataPayload.CODEC);
        registerDefaultPlayChannel(StringPayload.TYPE, StringPayload.CODEC);
        registerDefaultPlayChannel(CarpetPayload.TYPE, CarpetPayload.CODEC);
        registerDefaultPlayChannel(ServuxPayload.TYPE, ServuxPayload.CODEC);
        channelsInit = true;
    }
}
