package fi.dy.masa.malilib.network.payload;

import net.minecraft.util.Identifier;

/**
 * Used in the TYPES HashMap for registering Channels.
 * This helps provide a foolproof method to help with the work flow in a more abstract manner.
 * This class is where the Channel Identifier gets stored / created.
 * Then it can be referenced by the static Payload Records, allowing them to function a
 * little more dynamically.
 */
public class PayloadCodec implements IPayloadCodec
{
    private final PayloadType type;
    private final Identifier id;
    private boolean play_registered;

    protected PayloadCodec(PayloadType type, Identifier id)
    {
        this.type = type;
        this.id = id;
        this.play_registered = false;
    }

    public PayloadType getType() { return this.type; }
    public Identifier getId() { return this.id; }
    public void registerPlayCodec() { this.play_registered = true; }
    public boolean isPlayRegistered() { return this.play_registered; }
}
