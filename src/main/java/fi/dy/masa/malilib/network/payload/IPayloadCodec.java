package fi.dy.masa.malilib.network.payload;

import net.minecraft.util.Identifier;

/**
 * Provides an interface to the PayloadCodec class for getting various data on Payload / Channels
 * that have been registered with PayloadManager
 */
public interface IPayloadCodec
{
    PayloadType getType();
    Identifier getId();
    void registerPlayCodec();
    boolean isPlayRegistered();
}