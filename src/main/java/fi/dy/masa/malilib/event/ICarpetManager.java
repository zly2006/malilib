package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ICarpetListener;

public interface ICarpetManager
{
    /**
     * Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void registerCarpetHandler(ICarpetListener handler);

    /**
     * Un-Registers a handler for receiving Carpet Hello NBTCompound packets.
     * @param handler
     */
    void unregisterCarpetHandler(ICarpetListener handler);
}
