package fi.dy.masa.malilib.deprecated;

@Deprecated
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
