package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.event.CarpetHandler;
import fi.dy.masa.malilib.network.payload.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

public class ClientNetworkPlayHandler
{
    // String Payload
    public static void send(StringPayload payload)
    {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void receive(StringPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from the Server
        String response = payload.toString();
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): received S2CString Payload: {}", response);
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): id: {}, You were sent (STRING): {}", payload.getId(), response);
    }
    // Data Payload
    public static void send(DataPayload payload)
    {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void receive(DataPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from server
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): received S2CData Payload (size in bytes): {}", payload.data().getSizeInBytes());
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeByteArray((payload.data().getByteArray(DataPayload.NBT)));
        // --> To write a PacketByteBuf from NbtCompound
//        String response = payload.data().getString(DataPayload.NBT);
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): buf size in bytes: {}", buf.readableBytes());
        String response = buf.readString();
        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): id: {}, String: {}", payload.getId(), response);

        MaLiLib.printDebug("ClientNetworkPlayHandler#receive(): You were sent (DATA): {}", response);
    }
    public static void sendCarpet(CarpetPayload payload)
    {
        // Server-bound packet sent from the Client
        // --> Carpet server present
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("ClientNetworkPlayHandler#sendCarpet(): sending payload id: {}", payload.getId());
        }
    }
    public static void receiveCarpet(CarpetPayload payload, ClientPlayNetworking.Context ctx)
    {
        // Client-bound packet received from server
        // --> Carpet server present
        MaLiLib.printDebug("ClientNetworkPlayHandler#receiveCarpet(): id: {} received Carpet Payload (size in bytes): {}", payload.getId(), payload.data().getSizeInBytes());

        // Handle Carpet packet
        ((CarpetHandler) CarpetHandler.getInstance()).onCarpetPayload(payload.data(), ctx);
    }
}
