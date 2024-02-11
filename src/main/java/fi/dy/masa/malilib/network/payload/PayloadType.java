package fi.dy.masa.malilib.network.payload;

/**
 * Foolproof method for listing available Payload Types.  Types not listed are rejected.
 */
public enum PayloadType
{
    MALILIB_BYTE_BUF,
    CARPET_HELLO,
    SERVUX_STRUCTURES,
    SERVUX_METADATA,
    SERVUX_LITEMATICS;
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
