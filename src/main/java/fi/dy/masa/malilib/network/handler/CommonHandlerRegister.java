package fi.dy.masa.malilib.network.handler;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.channel.ServuxStructuresPayload;

/**
 * This class acts as a Middleware between PayloadManager, and the PlayHandler's
 */
public class CommonHandlerRegister
{
    private static final CommonHandlerRegister INSTANCE = new CommonHandlerRegister();
    public static CommonHandlerRegister getInstance() { return INSTANCE; }

    public <T extends CustomPayload> void registerPlayHandler(CustomPayload.Id<T> type, ClientPlayNetworking.PlayPayloadHandler<T> handler)
    {
        if (MaLiLibReference.isClient())
        {
            MaLiLib.printDebug("CommonHandlerRegister#registerPlayHandler(): for type {}", type.id().toString());
            ClientPlayNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void registerPlayHandler(CustomPayload.Id<T> type, ServerPlayNetworking.PlayPayloadHandler<T> handler)
    {
        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            MaLiLib.printDebug("CommonHandlerRegister#registerPlayHandler(): for type {}", type.id().toString());
            ServerPlayNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void unregisterPlayHandler(CustomPayload.Id<T> type)
    {
        MaLiLib.printDebug("CommonHandlerRegister#unregisterPlayHandler(): for type {}", type.id().toString());

        if (MaLiLibReference.isClient())
        {
            ClientPlayNetworking.unregisterGlobalReceiver(type.id());
        }

        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            ServerPlayNetworking.unregisterGlobalReceiver(type.id());
        }
    }

    // TODO --> An entry here needs to exist for every MaLiLib Payload type
    @SuppressWarnings("unchecked")
    public <T extends CustomPayload> CustomPayload.Id<T> getPayloadType(PayloadType type)
    {
        if (type == PayloadType.SERVUX_STRUCTURES)
        {
            return (CustomPayload.Id<T>) ServuxStructuresPayload.TYPE;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <B extends ByteBuf, T extends CustomPayload> PacketCodec<B, T> getPacketCodec(PayloadType type)
    {
        if (type == PayloadType.SERVUX_STRUCTURES)
        {
            return (PacketCodec<B, T>) ServuxStructuresPayload.CODEC;
        }
        else
        {
            return null;
        }
    }
}
