package fi.dy.masa.malilib.network.test;

import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Set;

@Deprecated
public class ServerDebugSuite
{
    public static void checkGlobalPlayChannels()
    {
        MaLiLib.printDebug("ServerDebugSuite#checkGlobalPlayChannels(): Start.");
        Set<Identifier> channels = ServerPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("ServerDebugSuite#checkGlobalPlayChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("ServerDebugSuite#checkGlobalPlayChannels(): END. Total Channels: "+i);
    }

    public static void checkGlobalConfigChannels()
    {
        MaLiLib.printDebug("ServerDebugSuite#checkGlobalConfigChannels(): Start.");
        Set<Identifier> channels = ServerConfigurationNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            MaLiLib.printDebug("ServerDebugSuite#checkGlobalConfigChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        MaLiLib.printDebug("ServerDebugSuite#checkGlobalConfigChannels(): END. Total Channels: "+i);
    }
}
