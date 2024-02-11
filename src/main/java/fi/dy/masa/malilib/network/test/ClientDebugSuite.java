package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Set;

/**
 * Simple Debug Logger test to display all registered Global Play channels that extends CustomPayload in this static context
 */
//@Deprecated
public class ClientDebugSuite
{
    public static void checkGlobalPlayChannels()
    {
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalPlayChannels(): Start.");
        Set<Identifier> channels = ClientPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("ClientDebugSuite#checkGlobalPlayChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalPlayChannels(): END. Total Channels: "+i);
    }
    public static void checkGlobalConfigChannels()
    {
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalConfigChannels(): Start.");
        Set<Identifier> channels = ClientConfigurationNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("ClientDebugSuite#checkGlobalConfigChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("ClientDebugSuite#checkGlobalConfigChannels(): END. Total Channels: "+i);
    }
}
