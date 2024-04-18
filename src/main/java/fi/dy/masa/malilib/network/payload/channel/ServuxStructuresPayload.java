package fi.dy.masa.malilib.network.payload.channel;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malilib.network.payload.PayloadManager;
import fi.dy.masa.malilib.network.payload.PayloadType;

/**
 * ServuX Structures Data Provider; the actual Payload shared between MiniHUD and ServuX
 */
public record ServuxStructuresPayload(NbtCompound data) implements CustomPayload
{
    public static final Id<ServuxStructuresPayload> TYPE = new Id<>(PayloadManager.getInstance().getIdentifier(PayloadType.SERVUX_STRUCTURES));
    public static final PacketCodec<PacketByteBuf, ServuxStructuresPayload> CODEC = CustomPayload.codecOf(ServuxStructuresPayload::write, ServuxStructuresPayload::new);

    public ServuxStructuresPayload(PacketByteBuf buf) { this(buf.readNbt()); }

    private void write(PacketByteBuf buf) { buf.writeNbt(data); }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
