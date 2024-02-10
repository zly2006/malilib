package fi.dy.masa.malilib.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;

/**
 * This can be used as a replacement for your legacy "PacketByteBuf" type of CustomPayloads,
 * And can be used to try to (re)-implement your IPluginChannelHandler
 */
public class MaLibBuf extends PacketByteBuf
{
    public MaLibBuf(ByteBuf parent)
    {
        super(parent);
    }
}