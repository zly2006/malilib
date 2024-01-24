package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class S2CStringHandler
{
    public static void send(S2CStringPayload payload, ServerPlayerEntity player) {
        // Client-Bound packet sent from the Server
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CStringListener#send(): sending payload id: {}", payload.getId());
        }
    }

    public static void receive(S2CStringPayload payload, ClientPlayNetworking.Context ctx) {
        // Client-bound packet received from the Server
        String response = payload.toString();
        MaLiLib.printDebug("S2CStringListener#receive(): received S2CString Payload: {}", response);
        ctx.player().sendMessage(Text.of("Received a message from the server."));
        ctx.player().sendMessage(Text.of("You were sent (STRING): "+response));
    }
}
