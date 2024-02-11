package fi.dy.masa.malilib.deprecated;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

@Deprecated
public interface ICarpetHelloListener
{
    /**
     * Called by the Fabric Networking API when a Carpet Hello Payload is received on a registered channel
     * @param data (The Carpet Hello payload)
     * @param ctx (Client context)
     */
    default void receiveCarpetHello(NbtCompound data, ClientPlayNetworking.Context ctx) { }
    /**
     * Can be used by downstream mod
     * to send a Carpet Hello payload --> they should just call ClientNetworkPlayHandler.sendCarpetHello()
     * themselves though.
     * @param data (The Carpet Hello payload to send)
     */
    default void sendCarpetHello(NbtCompound data) { }
    // No encoding required since it's only listening for a Carpet Hello packet.
}
