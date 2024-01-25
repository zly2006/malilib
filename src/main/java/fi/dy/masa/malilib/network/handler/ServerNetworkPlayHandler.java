package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class ServerNetworkPlayHandler
{
    // String Payloads
    public static void send(S2CStringPayload payload, ServerPlayerEntity player)
    {
        // Client-Bound packet sent from the Server
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CStringListener#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void receive(C2SStringPayload payload, ServerPlayNetworking.Context ctx)
    {
        // Server-bound packet received from the Client
        String response = payload.toString();
        MaLiLib.printDebug("C2SStringListener#receive(): received C2SString Payload: {}", response);
        ctx.player().sendMessage(Text.of("Your message has been received by the server:"));
        ctx.player().sendMessage(Text.of("You sent (STRING) me: "+response));
    }
    // Data Payloads
    public static void send(S2CDataPayload payload, ServerPlayerEntity player)
    {
        // Client-bound packet sent from the Server
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CDataListener#send(): sending payload id: {}", payload.getId());
        }
    }

    public static void receive(C2SDataPayload payload, ServerPlayNetworking.Context ctx)
    {
        // Server-bound packet received from the Client
        MaLiLib.printDebug("C2SDataListener#receive(): received C2SData Payload (size in bytes): {}", payload.data().readableBytes());
        MaLiLib.printDebug("C2SDataListener#receive(): id: {}", payload.data().readIdentifier());
        String response = payload.data().readString();
        MaLiLib.printDebug("C2SDataListener#receive(): String: {}", response);
        ctx.player().sendMessage(Text.of("Your message has been received by the server:"));
        ctx.player().sendMessage(Text.of("You sent (DATA) to me: "+response));
    }
}
