package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.handler.ClientCommonHandlerRegister;
import fi.dy.masa.malilib.network.handler.play.ClientPlayHandler;
import fi.dy.masa.malilib.network.handler.play.IPluginPlayHandler;
import fi.dy.masa.malilib.network.payload.PayloadCodec;
import fi.dy.masa.malilib.network.payload.PayloadType;
import fi.dy.masa.malilib.network.payload.PayloadTypeRegister;
import fi.dy.masa.malilib.network.payload.channel.CarpetHelloPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

public abstract class CarpetHelloPlayListener_example<T extends CustomPayload> implements IPluginPlayHandler<T>
{
    public final static CarpetHelloPlayListener_example<CarpetHelloPayload> INSTANCE = new CarpetHelloPlayListener_example<>()
    {
        @Override
        public void receive(CarpetHelloPayload payload, ClientPlayNetworking.Context context)
        {
            ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
            CallbackInfo ci = new CallbackInfo("CarpetHelloPlayListener_example", false);

            //MaLiLib.printDebug("CarpetHelloPlayListener_example#receive(): invoked.");

            if (handler != null)
            {
                CarpetHelloPlayListener_example.INSTANCE.receiveS2CPlayPayload(PayloadType.CARPET_HELLO, payload, handler, ci);
                // networkHandler interface must be used for Carpet Server
                //  because they don't use Fabric API.
            }
            else
                CarpetHelloPlayListener_example.INSTANCE.receiveS2CPlayPayload(PayloadType.CARPET_HELLO, payload, context);
        }
    };
    private final Map<PayloadType, Boolean> registered = new HashMap<>();
    private final boolean carpetRespond = false;
    private boolean carpetRegister;
    private String carpetVersion;
    @Override
    public PayloadType getPayloadType() { return PayloadType.CARPET_HELLO; }

    @Override
    public void reset(PayloadType type)
    {
        // Don't unregister
        this.carpetRegister = false;
        this.carpetVersion = "";
        CarpetHelloPlayListener_example.INSTANCE.unregisterPlayHandler(type);
        if (this.registered.containsKey(type))
            this.registered.replace(type, false);
        else
            this.registered.put(type, false);
    }

    @Override
    public <P extends CustomPayload> void receiveS2CPlayPayload(PayloadType type, P payload, ClientPlayNetworking.Context ctx)
    {
        MaLiLib.printDebug("CarpetHelloPlayListener_example#receiveS2CPlayPayload(): handling packet via Fabric Network API.");

        CarpetHelloPayload packet = (CarpetHelloPayload) payload;
        ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).decodeS2CNbtCompound(PayloadType.CARPET_HELLO, packet.data());
    }

    @Override
    public <P extends CustomPayload> void receiveS2CPlayPayload(PayloadType type, P payload, ClientPlayNetworkHandler handler, CallbackInfo ci)
    {
        // Store the network handler here if wanted
        MaLiLib.printDebug("CarpetHelloPlayListener_example#receiveS2CPlayPayload(): handling packet via network handler interface.");

        CarpetHelloPayload packet = (CarpetHelloPayload) payload;
        ((ClientPlayHandler<?>) ClientPlayHandler.getInstance()).decodeS2CNbtCompound(PayloadType.CARPET_HELLO, packet.data());

        if (ci.isCancellable() && !MaLiLibReference.hasCarpetClient())
            ci.cancel();
    }

    @Override
    public void decodeS2CNbtCompound(PayloadType type, NbtCompound data)
    {
        // Handle packet.
        if (!this.carpetRegister)
        {
            String carpetVersion = data.getString(PacketType_example.CarpetHello.HI);
            if (carpetVersion.contains("malilib"))
                MaLiLib.printDebug("CarpetHelloPlayListener_example#decodeS2CNbtCompound(): received [FAKE] Carpet Hello packet. (Carpet Server {})", carpetVersion);
            else
                MaLiLib.printDebug("CarpetHelloPlayListener_example#decodeS2CNbtCompound(): received [REAL] Carpet Hello packet. (Carpet Server {})", carpetVersion);

            // We have a Carpet server.
            this.carpetRegister = true;
            this.carpetVersion = carpetVersion;
            this.registered.replace(type, true);

            // Respond to Carpet's HI packet.  Set to false if you don't want to participate.
            if (this.carpetRespond && !MaLiLibReference.hasCarpetClient())
            {
                //  We can fully implement various Carpet Hello packets from here on out directly,
                //  after handling the "CarpetRules" packet, if wanted.
                NbtCompound nbt = new NbtCompound();
                nbt.putString(PacketType_example.CarpetHello.HELLO, MaLiLibReference.MOD_ID + "-" + MaLiLibReference.MOD_VERSION);
                CarpetHelloPlayListener_example.INSTANCE.encodeC2SNbtCompound(type, nbt);
            }
        }
        else
        {
            // Handle additional Carpet Packets ?
            if (!MaLiLibReference.hasCarpetClient())
                MaLiLib.printDebug("CarpetHelloPlayListener_example#decodeS2CNbtCompound(): received unhandled Carpet Hello packet. (size: {})", data.getSizeInBytes());
            // Ignore
            //else
                //MaLiLib.printDebug("CarpetHelloPlayListener_example#decodeS2CNbtCompound(): ignoring Carpet Hello packet. (size: {}) (Carpet Client is present)", data.getSizeInBytes());
        }
    }

    @Override
    public void encodeC2SNbtCompound(PayloadType type, NbtCompound data)
    {
        // Encode Payload
        CarpetHelloPayload newPayload = new CarpetHelloPayload(data);

        // NetworkHandler method first for carpet servers
        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler != null)
            CarpetHelloPlayListener_example.INSTANCE.sendC2SPlayPayload(type, newPayload, handler);
        else
            CarpetHelloPlayListener_example.INSTANCE.sendC2SPlayPayload(type, newPayload);
    }
    //@Override
    public void sendC2SPlayPayload(PayloadType type, CarpetHelloPayload payload)
    {
        if (ClientPlayNetworking.canSend(payload.getId()))
        {
            ClientPlayNetworking.send(payload);
        }
        else
            MaLiLib.printDebug("CarpetHelloPlayListener_example#sendC2SPlayPayload(): [ERROR] canSend = false;");
    }
    //@Override
    public void sendC2SPlayPayload(PayloadType type, CarpetHelloPayload payload, ClientPlayNetworkHandler handler)
    {
        Packet<?> packet = new CustomPayloadC2SPacket(payload);

        if (handler == null)
        {
            MaLiLib.printDebug("CarpetHelloPlayListener_example#sendC2SPlayPayload(): [ERROR] networkHandler = null");
            return;
        }
        if (handler.accepts(packet))
        {
            handler.sendPacket(packet);
        }
        else
            MaLiLib.printDebug("CarpetHelloPlayListener_example#sendC2SPlayPayload(): [ERROR] accepts() = false");
    }
    @Override
    public void registerPlayPayload(PayloadType type)
    {
        PayloadCodec codec = PayloadTypeRegister.getInstance().getPayloadCodec(type);

        if (codec == null)
        {
            return;
        }
        if (!codec.isPlayRegistered())
        {
            MaLiLib.printDebug("CarpetHelloPlayListener_example#registerPlayPayload(): received for type {}", type.toString());

            // Don't register carpet:hello --> This will Break CarpetClient from working (if installed)
            if (MaLiLibReference.hasCarpetClient())
            {
                // Fake register it.
                codec.registerPlayCodec();
            }
            else
            {
                // Real register it.
                PayloadTypeRegister.getInstance().registerPlayChannel(type, ClientCommonHandlerRegister.getInstance().getPayloadType(type), ClientCommonHandlerRegister.getInstance().getPacketCodec(type));
            }
        }
        //ClientDebugSuite.checkGlobalPlayChannels();
    }
    @Override
    @SuppressWarnings("unchecked")
    public void registerPlayHandler(PayloadType type)
    {
        PayloadCodec codec = PayloadTypeRegister.getInstance().getPayloadCodec(type);

        if (codec == null)
        {
            return;
        }
        if (codec.isPlayRegistered())
        {
            //MaLiLib.printDebug("CarpetHelloPlayListener_example#registerPlayHandler(): received for type {}", type.toString());

            // Don't register carpet:hello --> This will Break CarpetClient from working!
            if (!MaLiLibReference.hasCarpetClient())
                ClientCommonHandlerRegister.getInstance().registerPlayHandler((CustomPayload.Id<T>) CarpetHelloPayload.TYPE, this);
            if (this.registered.containsKey(type))
                this.registered.replace(type, true);
            else
                this.registered.put(type, true);
        }
        //ClientDebugSuite.checkGlobalPlayChannels();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void unregisterPlayHandler(PayloadType type)
    {
        PayloadCodec codec = PayloadTypeRegister.getInstance().getPayloadCodec(type);

        if (codec == null)
        {
            return;
        }
        if (codec.isPlayRegistered())
        {
            //MaLiLib.printDebug("CarpetHelloPlayListener_example#unregisterPlayHandler(): received for type {}", type.toString());

            // Don't register carpet:hello --> This will Break CarpetClient from working!
            if (!MaLiLibReference.hasCarpetClient())
                ClientCommonHandlerRegister.getInstance().unregisterPlayHandler((CustomPayload.Id<T>) CarpetHelloPayload.TYPE);
            if (this.registered.containsKey(type))
                this.registered.replace(type, false);
            else
                this.registered.put(type, false);
        }
    }
}
