package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Set;

/**
 * Simple Debug Logger test to display all registered Global Play channels that extends CustomPayload in this static context
 */
public class ClientDebugSuite {
    public static void checkGlobalChannels() {
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalChannels(): Start.");
        Set<Identifier> channels = ClientPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("ClientDebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    }
}
