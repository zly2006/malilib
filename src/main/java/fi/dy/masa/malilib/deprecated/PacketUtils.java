package fi.dy.masa.malilib.deprecated;

import java.util.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

/**
 * This isn't currently being used, because most modern 1.20.5+ packets aren't using PacketByteBuf directly.
 * Saved for historical purposes.
 */
@Deprecated(forRemoval = false)
public class PacketUtils
{
    /**
     * Wraps the newly created buf from {@code buf.slice()} in a PacketByteBuf.
     *
     * @param buf the original ByteBuf
     * @return a slice of the buffer
     * @see ByteBuf#slice()
     */
    public static PacketByteBuf slice(ByteBuf buf)
    {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");
        return new PacketByteBuf(buf.slice());
    }

    /**
     * Wraps the newly created buf from {@code buf.retainedSlice()} in a PacketByteBuf.
     *
     * @param buf the original ByteBuf
     * @return a slice of the buffer
     * @see ByteBuf#retainedSlice()
     */
    public static PacketByteBuf retainedSlice(ByteBuf buf)
    {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");
        return new PacketByteBuf(buf.retainedSlice());
    }
}
