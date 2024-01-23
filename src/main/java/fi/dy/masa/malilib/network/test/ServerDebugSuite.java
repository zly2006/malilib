package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Set;

public class ServerDebugSuite {
    public static void checkGlobalChannels() {
        MaLiLib.printDebug("DebugSuite#checkGlobalChannels(): Start.");
        Set<Identifier> channels = ServerPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("DebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("DebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    }
}
