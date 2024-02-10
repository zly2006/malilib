package fi.dy.masa.malilib.mixin;

import javax.annotation.Nullable;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.ClientNetworkPlayInitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import fi.dy.masa.malilib.event.WorldLoadHandler;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler
{
    @Shadow private ClientWorld world;

    @Unique
    @Nullable private ClientWorld worldBefore;

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void malilib_onPreJoinGameHead(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        // Need to grab the old world reference at the start of the method,
        // because the next injection point is right after the world has been assigned,
        // since we need the new world reference for the callback.
        this.worldBefore = this.world;
    }

    @Inject(method = "onGameJoin", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/MinecraftClient;joinWorld(" +
                         "Lnet/minecraft/client/world/ClientWorld;)V"))
    private void malilib_onPreGameJoin(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        // Call only in case channels aren't registered.
        ((WorldLoadHandler) WorldLoadHandler.getInstance()).onWorldLoadPre(this.worldBefore, this.world, MinecraftClient.getInstance());

        // Abstract maybe in the future?
        ClientNetworkPlayInitHandler.registerPlayChannels();
        MaLiLib.printDebug("malilib_onPreGameJoin()");
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void malilib_onPostGameJoin(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        // Register receivers
        ((WorldLoadHandler) WorldLoadHandler.getInstance()).onWorldLoadPost(this.worldBefore, this.world, MinecraftClient.getInstance());
        this.worldBefore = null;

        // Abstract maybe in the future?
        ClientNetworkPlayInitHandler.registerReceivers();
        MaLiLib.printDebug("malilib_onPostGameJoin()");
    }
}
