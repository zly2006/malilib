package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.handler.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.handler.ServerNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.StringPayload;
import fi.dy.masa.malilib.network.payload.DataPayload;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class TestSuite {
    public static void testS2C(ServerPlayerEntity player, String msg)
    {
        // Server -> Client
        if (MaLiLibReference.isServer()) {
            // String test
            MaLiLib.printDebug("TestSuite#testS2C() executing S2CString test packet.");
            StringPayload S2CTest1 = new StringPayload(msg);
            ServerNetworkPlayHandler.send(S2CTest1, player);

            // DATA Test
            MaLiLib.printDebug("TestSuite#testS2C() executing S2CData (String encapsulated) test packet.");
            NbtCompound nbt = new NbtCompound();
            PacketByteBuf buf =  new PacketByteBuf(Unpooled.buffer());
            buf.writeString(msg);
            nbt.putByteArray(DataPayload.NBT, buf.readByteArray());

//            nbt.putString(DataPayload.NBT, msg);
            DataPayload S2CTest2 = new DataPayload(nbt);
            ServerNetworkPlayHandler.send(S2CTest2, player);
        }
        else
            MaLiLib.printDebug("TestSuite#testS2C() called from a Client Environment.");
    }
    public static void testC2S(String msg)
    {
        // Client -> Server
        if (MaLiLibReference.isClient())
        {
            if (!MaLiLibReference.isSinglePlayer()) {
                // String test
                MaLiLib.printDebug("TestSuite#testC2S() executing C2SString test packet.");
                StringPayload C2STest1 = new StringPayload(msg);
                ClientNetworkPlayHandler.send(C2STest1);

                // DATA Test
                MaLiLib.printDebug("TestSuite#testC2S() executing C2SData (String encapsulated) test packet.");
                NbtCompound nbt = new NbtCompound();
                PacketByteBuf buf =  new PacketByteBuf(Unpooled.buffer());
                buf.writeString(msg);
                nbt.putByteArray(DataPayload.NBT, buf.readByteArray());
                //nbt.putString(DataPayload.NBT, msg);
                DataPayload C2STest2 = new DataPayload(nbt);
                ClientNetworkPlayHandler.send(C2STest2);
            }
            else
                MaLiLib.printDebug("TestSuite#testC2S() called from Single Player Mode. (No Server to send packets to).");
        }
        else
            MaLiLib.printDebug("TestSuite#testC2S() called from a Server Environment.");
    }
    public static void initTestSuite()
    {
        // Register Payload types
        PayloadTypeRegister.initTypes(MaLiLibReference.MOD_ID);
        PayloadTypeRegister.registerPlayChannels();
        // Register test command
        CommandTest.registerCommandTest();
        // Setup callbacks for Server Environment
        // --> Client callbacks come from MixinMinecraftClient
        if (MaLiLibReference.isServer()) {
            ServerLifecycleEvents.SERVER_STARTED.register((id) -> ServerEvents.started());
            ServerLifecycleEvents.SERVER_STOPPING.register((id) -> ServerEvents.stopping());
        }
    }
}
