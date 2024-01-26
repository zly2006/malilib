package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.IServuxPayloadListener;

public interface IServuxPayloadManager
{
    /**
     * Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void registerServuxHandler(IServuxPayloadListener handler);

    /**
     * Un-Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void unregisterServuxHandler(IServuxPayloadListener handler);
}
