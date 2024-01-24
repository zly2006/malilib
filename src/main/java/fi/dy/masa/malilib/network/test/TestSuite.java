package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.network.PayloadTypeRegister;
import fi.dy.masa.malilib.network.handler.C2SStringHandler;
import fi.dy.masa.malilib.network.handler.S2CStringHandler;
import fi.dy.masa.malilib.network.payload.C2SStringPayload;
import fi.dy.masa.malilib.network.payload.S2CStringPayload;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class TestSuite {
    public static void testS2C(ServerPlayerEntity player, String msg)
    {
        if (MaLiLibReference.isServer()) {
            S2CStringPayload S2CTest1 = new S2CStringPayload(msg);
            S2CStringHandler.send(S2CTest1, player);

            /*
            PacketByteBuf buf =  new PacketByteBuf(Unpooled.buffer());
            Identifier id = Identifier.of("testS2C", "payload");
            buf.writeIdentifier(id);
            buf.writeString(msg);
            S2CDataPayload S2CTest2 = new S2CDataPayload(id, buf);
            S2CHandler.send(player, S2CTest2);

             */
        }
        else
            MaLiLib.printDebug("TestSuite#testS2C() called from a Client Environment.");
    }
    public static void testC2S(String msg)
    {
        if (MaLiLibReference.isClient())
        {
            C2SStringPayload C2STest1 = new C2SStringPayload(msg);
            C2SStringHandler.send(C2STest1);
/*
            PacketByteBuf buf =  new PacketByteBuf(Unpooled.buffer());
            Identifier id = Identifier.of("testC2S", "payload");
            buf.writeIdentifier(id);
            buf.writeString(msg);
            C2SDataPayload S2CTest2 = new C2SDataPayload(id, buf);
            C2SHandler.send(S2CTest2);

 */
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
