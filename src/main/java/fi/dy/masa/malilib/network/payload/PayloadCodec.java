package fi.dy.masa.malilib.network.payload;

import net.minecraft.util.Identifier;

/**
 * Used in the TYPES HashMap for registering Channels.
 * This helps provide a more foolproof method to help with the work flow in a more abstract manner,
 * And this class is where the actual Channel Identifier gets stored / created, which
 * can be referenced by the static Payload Records.
 */
public class PayloadCodec implements IPayloadCodec
{
    private final PayloadType type;
    private final Identifier id;
    private boolean play_registered;

    protected PayloadCodec(PayloadType type, String namespace, String path)
    {
        this.type = type;
        this.id = new Identifier(namespace, path);
        this.play_registered = false;
    }

    public PayloadType getType() { return this.type; }
    public Identifier getId() { return this.id; }
    public void registerPlayCodec() { this.play_registered = true; }
    public boolean isPlayRegistered() { return this.play_registered; }
}
