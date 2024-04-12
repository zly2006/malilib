package fi.dy.masa.malilib.network.handler.server;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;

public interface IServerNetworkBase
{
     <H extends ServerCommonNetworkHandler> void handleServerPayload(H handler, CustomPayloadC2SPacket packet, MinecraftServer server, CallbackInfo ci);
}
