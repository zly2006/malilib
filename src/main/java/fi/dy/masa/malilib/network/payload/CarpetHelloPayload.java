package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.network.PayloadType;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record CarpetHelloPayload(NbtCompound data) implements CustomPayload
{
    public static final Id<CarpetHelloPayload> TYPE = new Id<>(PayloadTypeRegister.getIdentifier(PayloadType.CARPET_HELLO));
    public static final PacketCodec<PacketByteBuf, CarpetHelloPayload> CODEC = CustomPayload.codecOf(CarpetHelloPayload::write, CarpetHelloPayload::new);
    public static final String KEY = PayloadTypeRegister.getKey(PayloadType.CARPET_HELLO);

    public CarpetHelloPayload(PacketByteBuf buf) { this(buf.readNbt()); }
    private void write(PacketByteBuf buf) { buf.writeNbt(data); }
    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
