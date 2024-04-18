package fi.dy.masa.malilib.network.payload;

/**
 * Foolproof method for listing available Payload Types.
 * All types not listed here are rejected.
 */
public enum PayloadType
{
    // Simply add to this ENUM list if you wish to create more Payload Types
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
