package fi.dy.masa.malilib.network.payload;

/**
 * Foolproof method for listing available Payload Types.
 * The API rejects all types not listed here.
 */
public enum PayloadType
{
    SERVUX_STRUCTURES;

    public boolean exists(PayloadType type)
    {
        for (final PayloadType p : PayloadType.values())
        {
            if (p == type)
            {
                return true;
            }
        }

        return false;
    }
}
