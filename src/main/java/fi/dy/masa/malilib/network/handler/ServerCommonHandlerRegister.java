package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.channel.*;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ServerCommonHandlerRegister
{
    public static final ServerCommonHandlerRegister INSTANCE = new ServerCommonHandlerRegister();
    public static ServerCommonHandlerRegister getInstance() { return INSTANCE; }

    public <T extends CustomPayload> void registerPlayHandler(CustomPayload.Id<T> type, ServerPlayNetworking.PlayPayloadHandler<T> handler)
    {
        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            MaLiLib.printDebug("ServerCommonHandlerRegister#registerPlayHandler(): for type {}", type.id().toString());
            ServerPlayNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void unregisterPlayHandler(CustomPayload.Id<T> type)
    {
        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            MaLiLib.printDebug("ServerCommonHandlerRegister#unregisterPlayHandler(): for type {}", type.id().toString());
            ServerPlayNetworking.unregisterGlobalReceiver(type.id());
        }
    }

    public <T extends CustomPayload> void registerConfigHandler(CustomPayload.Id<T> type, ServerConfigurationNetworking.ConfigurationPacketHandler<T> handler)
    {
        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            MaLiLib.printDebug("ServerCommonHandlerRegister#registerConfigHandler(): for type {}", type.id().toString());
            ServerConfigurationNetworking.registerGlobalReceiver(type, handler);
        }
    }

    public <T extends CustomPayload> void unregisterConfigHandler(CustomPayload.Id<T> type)
    {
        if (MaLiLibReference.isServer() || MaLiLibReference.isDedicated() || MaLiLibReference.isOpenToLan())
        {
            MaLiLib.printDebug("ServerCommonHandlerRegister#unregisterConfigHandler(): for type {}", type.id().toString());
            ServerConfigurationNetworking.unregisterGlobalReceiver(type.id());
        }
    }

    // TODO --> An entry here needs to exist for every MaLiLib Payload type
    @SuppressWarnings("unchecked")
    public <T extends CustomPayload> CustomPayload.Id<T> getPayloadType(PayloadType type)
    {
        //Malilib.printDebug("ServerCommonHandlerRegister#getPayload(): type {}", type.toString());
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
        //Malilib.printDebug("ServerCommonHandlerRegister#getPacketCodec(): type {}", type.toString());
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
