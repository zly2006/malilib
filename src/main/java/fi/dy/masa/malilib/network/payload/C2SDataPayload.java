package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.PayloadTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record C2SDataPayload(Identifier id, PacketByteBuf data) implements CustomPayload
{
    public static final Id<C2SDataPayload> TYPE;
    public static final PacketCodec<PacketByteBuf, C2SDataPayload> CODEC;

    private C2SDataPayload(PacketByteBuf buf)
    {
        this(buf.readIdentifier(), (PacketByteBuf) buf.readNullable((bufx) -> bufx.readBytes(PayloadTypeRegister.MAX_TOTAL_PER_PACKET_C2S)));
    }
    public C2SDataPayload(Identifier id, PacketByteBuf data)
    {
        this.id = id;
        this.data = data;
    }
    private void write(PacketByteBuf buf)
    {
        buf.writeIdentifier(this.id);
        buf.writeBytes(this.data);
    }
    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
    static
    {
        TYPE = new Id<>(PayloadTypeRegister.getIdentifier(PayloadTypes.PayloadType.C2S_DATA));
        CODEC = CustomPayload.codecOf(C2SDataPayload::write, C2SDataPayload::new);
    }
}
