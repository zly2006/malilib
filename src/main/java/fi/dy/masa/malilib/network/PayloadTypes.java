package fi.dy.masa.malilib.network;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.C2SDataPayload;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PayloadTypes
{
    public static final int MAX_TOTAL_PER_PACKET_S2C = 1048576;
    public static final int MAX_TOTAL_PER_PACKET_C2S = 32767;
    public static final String NAMESPACE_DEFAULT = MaLiLibReference.MOD_ID;
    private static String NAMESPACE = NAMESPACE_DEFAULT;
    // Probably need to implement an "Instance" method for NAMESPACE
    @Nullable
    public static Identifier getPayloadId(String name, String path)
    {
        MaLiLib.printDebug("PayloadTypes#getPayloadId(): name: {} path: {}", name, path);
        if (checkName(name))
        {
            final Identifier id = new Identifier(name, path);
            MaLiLib.printDebug("PayloadTypes#getPayloadId(): id namespace: {} path: {}", id.getNamespace(), id.getPath());
            return id;
        }
        else return null;
    }
    private static boolean checkName(String name)
    {
        if (NAMESPACE.isEmpty())
        {
            // Allow it to be initialized for this instance
            NAMESPACE = name;
            return true;
        }
        else return NAMESPACE.equals(name);
    }
    protected static void setNamespace(String name) {
        NAMESPACE = name;
    }

    public static String getNamespace() {
        return NAMESPACE;
    }

    public static void registerDefaultTypes()
    {
        MaLiLib.printDebug("PayloadTypes#registerDefaultTypes(): Namespace: "+getNamespace());
        setNamespace(MaLiLibReference.MOD_ID);

        MaLiLib.printDebug("PayloadTypes#registerDefaultTypes(): registerType()/Codec()");

        // Register Payloads (PLAY Channel)
        PayloadTypeRegistry.playC2S().register(C2SStringPayload.TYPE, C2SStringPayload.CODEC); // Client
        PayloadTypeRegistry.playS2C().register(S2CStringPayload.TYPE, S2CStringPayload.CODEC); // Server
        PayloadTypeRegistry.playC2S().register(C2SDataPayload.TYPE, C2SDataPayload.CODEC); // Client
        PayloadTypeRegistry.playS2C().register(S2CDataPayload.TYPE, S2CDataPayload.CODEC); // Server
        MaLiLib.printDebug("PayloadTypes#registerDefaultTypes(): Done.");
    }
}
