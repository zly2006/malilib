package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class S2CDataHandler
{
    public static void send(S2CDataPayload payload, ServerPlayerEntity player) {
        // Client-bound packet sent from the Server
        if (ServerPlayNetworking.canSend(player, payload.getId()))
        {
            ServerPlayNetworking.send(player, payload);
            MaLiLib.printDebug("S2CDataListener#send(): sending payload id: {}", payload.getId());
        }
    }

    public static void receive(S2CDataPayload payload, ClientPlayNetworking.Context ctx) {
        // Client-bound packet received from server
        MaLiLib.printDebug("S2CDataListener#receive(): received S2CData Payload (size in bytes): {}", payload.data().readableBytes());
        MaLiLib.printDebug("S2CDataListener#receive(): id: {}", payload.data().readIdentifier());
        String response = payload.data().readString();
        MaLiLib.printDebug("S2CDataListener#receive(): String: {}", response);
        ctx.player().sendMessage(Text.of("Received a message from the server."));
        ctx.player().sendMessage(Text.of("You were sent (DATA): "+response));
    }
}
