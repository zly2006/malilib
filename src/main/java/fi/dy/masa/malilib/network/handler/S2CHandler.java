package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class S2CHandler
{
    public static void receive(S2CStringPayload payload, ClientPlayNetworking.Context context)
    {
        String response = payload.toString();
        MaLiLib.printDebug("S2CHandler#receive() A: received S2CString Payload: {}", response);
        context.player().sendMessage(Text.of("Received a message from the server."));
        context.player().sendMessage(Text.of("You were sent (STRING): "+response));
    }
    public static void receive(S2CDataPayload payload, ClientPlayNetworking.Context context)
    {
        MaLiLib.printDebug("S2CHandler#receive() B: received S2CData Payload (size in bytes): {}", payload.data().readableBytes());
        MaLiLib.printDebug("S2CHandler#receive() B: id: {}", payload.data().readIdentifier());
        String response = payload.data().readString();
        MaLiLib.printDebug("S2CHandler#receive() B: String: {}", response);
        context.player().sendMessage(Text.of("Received a message from the server."));
        context.player().sendMessage(Text.of("You were sent (DATA): "+response));
    }
    public static void send(ServerPlayerEntity player, S2CStringPayload payload) {
        if (ServerPlayNetworking.canSend(player, payload.getId())) {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CHandler#send(): sending payload id: {}", payload.getId());
        }
    }public static void send(S2CStringPayload payload)
    {
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("S2CHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void send(ServerPlayerEntity player, S2CDataPayload payload) {
        if (ServerPlayNetworking.canSend(player, payload.getId())) {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void send(S2CDataPayload payload)
    {
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("S2CHandler#send(): sending payload id: {}", payload.getId());
        }
    }
}
