package fi.dy.masa.malilib.network.payload;

import fi.dy.masa.malilib.network.PayloadType;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ServuxLitematicsPayload(NbtCompound data) implements CustomPayload
{
    public static final Id<ServuxLitematicsPayload> TYPE = new Id<>(PayloadTypeRegister.getIdentifier(PayloadType.SERVUX_LITEMATICS));
    public static final PacketCodec<PacketByteBuf, ServuxLitematicsPayload> CODEC = CustomPayload.codecOf(ServuxLitematicsPayload::write, ServuxLitematicsPayload::new);
    public static final String KEY = PayloadTypeRegister.getKey(PayloadType.SERVUX_LITEMATICS);

    public ServuxLitematicsPayload(PacketByteBuf buf) { this(buf.readNbt()); }

    private void write(PacketByteBuf buf) { buf.writeNbt(data); }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
