package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

public class C2SStringHandler
{
    public static void send(C2SStringPayload payload) {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId())) {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("C2SStringListener#send(): sending payload id: {}", payload.getId());
        }
    }

    public static void receive(C2SStringPayload payload, ServerPlayNetworking.Context ctx) {
        // Server-bound packet received from the Client
        String response = payload.toString();
        MaLiLib.printDebug("C2SStringListener#receive(): received C2SString Payload: {}", response);
        ctx.player().sendMessage(Text.of("Your message has been received by the server:"));
        ctx.player().sendMessage(Text.of("You sent (STRING) me: "+response));
    }
}
