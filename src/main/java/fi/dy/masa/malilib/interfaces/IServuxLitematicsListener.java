package fi.dy.masa.malilib.interfaces;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IServuxLitematicsListener
{
    /**
     * Used by downstream mods to "reset()" their register status of the listener
     */
    default void reset() { }
    /**
     * Used by downstream mods when they receive a ServuxLitematicsPayload to decode
     * @param data (Data contained in the payload)
     * @param ctx (Context packet is received by)
     * @param id (Pass the Payload Channel Id())
     */
    default void receiveServuxLitematics(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id) { }
    /**
     * Used by downstream mods when they decode a ServuxLitematicsPayload
     * @param data (Data contained in the payload)
     * @param id (Pass the Payload Channel Id())
     */
    default void decodeServuxLitematics(NbtCompound data, Identifier id) { }
    /**
     * Used by the downstream mod to encode the data for sending a ServuxLitematicsPayload
     * @param data (Data to be encapsulated in the Payload)
     * @param id (Pass the Payload Channel Id())
     */
    default void encodeServuxLitematics(NbtCompound data, Identifier id) { }
    /**
     * Used by the downstream mod to send an encoded ServuxLitematicsPayload
     * @param data (Data to be encapsulated in the Payload)
     */
    default void sendServuxLitematics(NbtCompound data) { }
}
