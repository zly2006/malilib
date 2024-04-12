package fi.dy.masa.malilib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malilib.network.handler.client.ClientNetworkListener;

/**
 * This effective Mixin point has moved around a few times, so just leave it in both
 */
@Mixin(value = ClientConfigurationNetworkHandler.class)
public class MixinClientCommonNetworkHandler
{
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void malilib$onCustomPayload(CustomPayload packet, CallbackInfo ci)
    {
        if (!MinecraftClient.getInstance().isOnThread())
        {
            return;
        }

        ClientNetworkListener.getInstance().handleClientPayload((ClientCommonNetworkHandler) (Object) this, packet, ci);
    }
}
