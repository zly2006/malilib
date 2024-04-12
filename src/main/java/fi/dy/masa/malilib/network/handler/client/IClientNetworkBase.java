package fi.dy.masa.malilib.network.handler.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.CustomPayload;

public interface IClientNetworkBase
{
     <H extends ClientCommonNetworkHandler> void handleClientPayload(H handler, CustomPayload payload, CallbackInfo ci);
}
