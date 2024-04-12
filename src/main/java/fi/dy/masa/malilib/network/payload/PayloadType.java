package fi.dy.masa.malilib.network.payload;

/**
 * Foolproof method for listing available Payload Types.
 * All types not listed here are rejected.
 */
public enum PayloadType
{
    // TODO Add to this list if you wish to create more Payloads
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
