package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.handler.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.handler.ServerNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.C2SDataPayload;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;
import fi.dy.masa.malilib.network.payload.S2CDataPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TestSuite {
    public static void testS2C(ServerPlayerEntity player, String msg)
    {
        // Server -> Client
        if (MaLiLibReference.isServer()) {
            // String test
            MaLiLib.printDebug("TestSuite#testS2C() executing S2CString test packet.");
            S2CStringPayload S2CTest1 = new S2CStringPayload(msg);
            ServerNetworkPlayHandler.send(S2CTest1, player);

            // DATA Test
            MaLiLib.printDebug("TestSuite#testS2C() executing S2CData (String encapsulated) test packet.");
            PacketByteBuf buf =  new PacketByteBuf(Unpooled.buffer());
            Identifier id = Identifier.of("testS2C", "payload");
            buf.writeIdentifier(id);
            buf.writeString(msg);
            S2CDataPayload S2CTest2 = new S2CDataPayload(id, buf);
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
                C2SStringPayload C2STest1 = new C2SStringPayload(msg);
                ClientNetworkPlayHandler.send(C2STest1);

                // DATA Test
                MaLiLib.printDebug("TestSuite#testC2S() executing C2SData (String encapsulated) test packet.");
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                Identifier id = Identifier.of("testC2S", "payload");
                buf.writeIdentifier(id);
                buf.writeString(msg);
                C2SDataPayload S2CTest2 = new C2SDataPayload(id, buf);
                ClientNetworkPlayHandler.send(S2CTest2);
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
