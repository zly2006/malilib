package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.C2SDataPayload;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class C2SHandler
{
    public static void receive(C2SStringPayload payload, ServerPlayNetworking.Context context)
    {
        String response = payload.toString();
        MaLiLib.printDebug("C2SHandler#receive() A: received C2SString Payload: {}", response);
        context.player().sendMessage(Text.of("Your message has been received by the server:"));
        context.player().sendMessage(Text.of("You sent (STRING) me: "+response));
    }
    public static void receive(C2SDataPayload payload, ServerPlayNetworking.Context context)
    {
        MaLiLib.printDebug("C2SHandler#receive() B: received C2SData Payload (size in bytes): {}", payload.data().readableBytes());
        MaLiLib.printDebug("C2SHandler#receive() B: id: {}", payload.data().readIdentifier());
        String response = payload.data().readString();
        MaLiLib.printDebug("C2SHandler#receive() B: String: {}", response);
        context.player().sendMessage(Text.of("Your message has been received by the server:"));
        context.player().sendMessage(Text.of("You sent (DATA) to me: "+response));
    }
    public static void send(C2SStringPayload payload) {
        if (ClientPlayNetworking.canSend(payload.getId())) {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("C2SHandler#send(): sending payload id: {}", payload.getId());
        }
    }
   public static void send(ServerPlayerEntity player, C2SStringPayload payload)
    {
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("C2SHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void send(C2SDataPayload payload) {
        if (ClientPlayNetworking.canSend(payload.getId())) {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("C2SHandler#send(): sending payload id: {}", payload.getId());
        }
    }
    public static void send(ServerPlayerEntity player, C2SDataPayload payload)
    {
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("C2SHandler#send(): sending payload id: {}", payload.getId());
        }
    }
}
