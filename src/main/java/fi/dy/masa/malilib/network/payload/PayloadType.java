package fi.dy.masa.malilib.network.payload;

/**
 * Foolproof method for listing available Payload Types.  Types not listed are rejected.
 */
public enum PayloadType
{
    MALILIB_BYTEBUF,
    CARPET_HELLO,
    SERVUX_BLOCKS,
    SERVUX_BYTEBUF,
    SERVUX_ENTITIES,
    SERVUX_LITEMATICS,
    SERVUX_METADATA,
    SERVUX_STRUCTURES;
    public boolean exists(PayloadType type)
    {
        for (final PayloadType p : PayloadType.values())
        {
            if (p == type)
                return true;
        }

        return false;
    }
}
