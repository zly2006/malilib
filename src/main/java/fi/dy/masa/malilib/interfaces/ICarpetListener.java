package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

public interface ICarpetListener
{
    default void onCarpetPayload(NbtCompound data, ClientPlayNetworking.Context ctx) { }
}
