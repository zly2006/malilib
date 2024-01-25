package fi.dy.masa.malilib.mixin;

import fi.dy.masa.malilib.event.MinecraftServerEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer
{
    /**
     * For invoking IntergratedServer() calls
     * @param ci
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z"), method = "runServer")
    private void malilib_onServerStarting(CallbackInfo ci)
    {
        MinecraftServerEvents.onServerStarting((MinecraftServer) (Object) this);
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;createMetadata()Lnet/minecraft/server/ServerMetadata;", ordinal = 0), method = "runServer")
    private void malilib_onServerStarted(CallbackInfo ci)
    {
        MinecraftServerEvents.onServerStarted((MinecraftServer) (Object) this);
    }
    @Inject(at = @At("HEAD"), method = "shutdown")
    private void malilib_onServerStopping(CallbackInfo info)
    {
        MinecraftServerEvents.onServerStopping((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "shutdown")
    private void malilib_onServerStopped(CallbackInfo info)
    {
        MinecraftServerEvents.onServerStopped((MinecraftServer) (Object) this);
    }
}
