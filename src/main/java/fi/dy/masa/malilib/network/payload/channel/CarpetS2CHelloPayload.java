package fi.dy.masa.malilib.network.payload.channel;

import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * Used for handling Mainline Carpet Mod's NBT packet channel
 */
public record CarpetS2CHelloPayload(NbtCompound data) implements CustomPayload
{
    public static final Id<CarpetS2CHelloPayload> TYPE = new Id<>(PayloadTypeRegister.INSTANCE.getIdentifier(PayloadType.CARPET_HELLO));
    public static final PacketCodec<PacketByteBuf, CarpetS2CHelloPayload> CODEC = CustomPayload.codecOf(CarpetS2CHelloPayload::write, CarpetS2CHelloPayload::new);
    public static final String KEY = PayloadTypeRegister.INSTANCE.getKey(PayloadType.CARPET_HELLO);

    public CarpetS2CHelloPayload(PacketByteBuf buf) { this(buf.readNbt()); }
    private void write(PacketByteBuf buf) { buf.writeNbt(data); }
    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
