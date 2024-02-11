package fi.dy.masa.malilib.deprecated;

@Deprecated
public interface IServuxStructuresManager
{
    /**
     * Registers a handler for receiving Carpet Hello NBTCompound packets.
     */
    void registerServuxStructuresHandler(IServuxStructuresListener handler);

    /**
     * Un-Registers a handler for receiving Carpet Hello NBTCompound packets.
     */
    void unregisterServuxStructuresHandler(IServuxStructuresListener handler);
}
