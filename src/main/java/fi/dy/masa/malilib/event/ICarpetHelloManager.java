package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ICarpetHelloListener;

public interface ICarpetHelloManager
{
    /**
     * Registers a handler for receiving Carpet Hello NBTCompound packets.
     */
    void registerCarpetHelloHandler(ICarpetHelloListener handler);

    /**
     * Un-Registers a handler for receiving Carpet Hello NBTCompound packets.
     */
    void unregisterCarpetHelloHandler(ICarpetHelloListener handler);
}
