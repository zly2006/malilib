package fi.dy.masa.malilib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import fi.dy.masa.malilib.network.handler.server.ServerNetworkListener;

/**
 * This effective Mixin point has moved around a few times, so leave it in both
 */
@Mixin(value = ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler
{
    @Inject(
            method = "onCustomPayload",
            at = @At("HEAD"),
            cancellable = true)
    private void onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci)
    {
        ServerNetworkListener.getInstance().handleServerPayload((ServerPlayNetworkHandler) (Object) this, packet, null, ci);
    }
}
