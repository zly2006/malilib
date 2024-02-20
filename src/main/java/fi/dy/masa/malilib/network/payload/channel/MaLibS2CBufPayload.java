package fi.dy.masa.malilib.network.payload.channel;

import fi.dy.masa.malilib.network.payload.MaLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * Example Payload Type for extending a PacketByteBuf
 */
public record MaLibS2CBufPayload(MaLibByteBuf byteBuf) implements CustomPayload
{
    public static final Id<MaLibS2CBufPayload> TYPE = new Id<>(PayloadTypeRegister.INSTANCE.getIdentifier(PayloadType.MALILIB_BYTEBUF));
    public static final PacketCodec<PacketByteBuf, MaLibS2CBufPayload> CODEC = CustomPayload.codecOf(MaLibS2CBufPayload::write, MaLibS2CBufPayload::new);

    public MaLibS2CBufPayload(PacketByteBuf input)
    {
        this(new MaLibByteBuf(input.readBytes(input.readableBytes())));
    }

    private void write(PacketByteBuf output)
    {
        output.writeBytes(byteBuf);
    }

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return TYPE;
    }
}
