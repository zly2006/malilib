package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

public interface ICarpetHelloListener
{
    default void receiveCarpetHello(NbtCompound data, ClientPlayNetworking.Context ctx) { }
    default void sendCarpetHello(NbtCompound data) { }
}
