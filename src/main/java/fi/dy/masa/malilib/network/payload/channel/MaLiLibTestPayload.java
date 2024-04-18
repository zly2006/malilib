package fi.dy.masa.malilib.network.payload.channel;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import fi.dy.masa.malilib.network.payload.PayloadType;

public record MaLiLibTestPayload(NbtCompound data) implements CustomPayload
{
    public static final CustomPayload.Id<MaLiLibTestPayload> TYPE = new CustomPayload.Id<>(PayloadManager.getInstance().getIdentifier(PayloadType.MALILIB_TEST));
    public static final PacketCodec<PacketByteBuf, MaLiLibTestPayload> CODEC = CustomPayload.codecOf(MaLiLibTestPayload::write, MaLiLibTestPayload::new);

    public MaLiLibTestPayload(PacketByteBuf buf) { this(buf.readNbt()); }

    private void write(PacketByteBuf buf) { buf.writeNbt(data); }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() { return TYPE; }
}
