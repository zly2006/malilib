package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.PayloadTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record DataPayload(NbtCompound data) implements CustomPayload
{
    public static final Id<DataPayload> TYPE = new Id<>(PayloadTypeRegister.getIdentifier(PayloadTypes.PayloadType.DATA));
    public static final PacketCodec<PacketByteBuf, DataPayload> CODEC = CustomPayload.codecOf(DataPayload::write, DataPayload::new);
    public static final String NBT = "data";

    public DataPayload(PacketByteBuf buf) { this(buf.readNbt()); }

    private void write(PacketByteBuf buf) { buf.writeNbt(data); }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
