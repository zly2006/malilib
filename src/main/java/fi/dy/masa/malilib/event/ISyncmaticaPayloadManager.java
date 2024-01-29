package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ISyncmaticaPayloadListener;

public interface ISyncmaticaPayloadManager
{
    /**
     * Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void registerSyncmaticaHandler(ISyncmaticaPayloadListener handler);

    /**
     * Un-Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void unregisterSyncmaticaHandler(ISyncmaticaPayloadListener handler);
}
