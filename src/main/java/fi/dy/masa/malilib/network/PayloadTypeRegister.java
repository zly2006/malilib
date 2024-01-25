package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;

public class PayloadTypeRegister
{
    // This is how it looks in the static context per a MOD, which must each include its own Custom Payload Records.
    // The send/receive handlers can be made into an interface.
    public static final int MAX_TOTAL_PER_PACKET_S2C = 1048576;
    public static final int MAX_TOTAL_PER_PACKET_C2S = 32767;
    private static final Map<PayloadTypes.PayloadType, PayloadTypes> TYPES = new HashMap<>();
    public static void registerPlayChannels()
    {
        MaLiLib.printDebug("PayloadTypeRegister#registerPlayChannels(): registering play channels.");
        PayloadTypeRegistry.playC2S().register(C2SDataPayload.TYPE, C2SDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(C2SStringPayload.TYPE, C2SStringPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CarpetPayload.TYPE, CarpetPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CDataPayload.TYPE, S2CDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CStringPayload.TYPE, S2CStringPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CarpetPayload.TYPE, CarpetPayload.CODEC);
        // For Carpet "hello" packet (NbtCompound type)
    }
    public static Identifier getIdentifier(PayloadTypes.PayloadType type)
    {
        Identifier id = TYPES.get(type).getIdentifier();
        MaLiLib.printDebug("PayloadTypeRegister#getIdentifier(): type: {}, id: {}.", type, id);
        return id;
    }
    public static void initTypes(String namespace)
    {
        MaLiLib.printDebug("PayloadTypeRegister#initTypes(): init PayloadTypes for {}.", namespace);
        TYPES.put(PayloadTypes.PayloadType.C2S_STRING, new PayloadTypes(PayloadTypes.PayloadType.C2S_STRING, namespace));
        TYPES.put(PayloadTypes.PayloadType.S2C_STRING, new PayloadTypes(PayloadTypes.PayloadType.S2C_STRING, namespace));
        TYPES.put(PayloadTypes.PayloadType.C2S_DATA,   new PayloadTypes(PayloadTypes.PayloadType.C2S_DATA,   namespace));
        TYPES.put(PayloadTypes.PayloadType.S2C_DATA,   new PayloadTypes(PayloadTypes.PayloadType.S2C_DATA,   namespace));
        TYPES.put(PayloadTypes.PayloadType.CARPET_HELLO,   new PayloadTypes(PayloadTypes.PayloadType.CARPET_HELLO, "carpet"));
        // For Carpet "hello" packet (NbtCompound type)
    }
}
