package fi.dy.masa.malilib.interfaces;

import java.net.InetAddress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

public interface IServerListener
{
    /**
     * Called to obtain your local Inet Address, if it is not yet known,
     * it should try to resolve it for you, or return localhost.
     */
    default InetAddress getLocalIpAddr() { return InetAddress.getLoopbackAddress(); }

    /**
     * Called at the initial occurrence of a MinecraftServer is starting up
     * @param server (The MinecraftServer object)
     */
    default void onServerStarting(MinecraftServer server) {}

    /**
     * Called when the local MinecraftServer is finished starting
     * @param server (The MinecraftServer object)
     */
    default void onServerStarted(MinecraftServer server) {}

    /**
     * Called when the local MinecraftServer is configured
     * @param server (The IntegratedServer object)
     */
    default void onServerIntegratedSetup(IntegratedServer server) {}

    /**
     * Called when the local MinecraftServer is configured for "OpenToLan"
     * @param server (The IntegratedServer object)
     */
    default void onServerOpenToLan(IntegratedServer server) {}

    /**
     * Called when the local MinecraftServer enters its initial "stopping" state
     * @param server (The MinecraftServer object)
     */
    default void onServerStopping(MinecraftServer server) {}

    /**
     * Called when the local MinecraftServer finishes it's "stopped" state and before the "server" object itself is killed.
     * @param server (The MinecraftServer object)
     */
    default void onServerStopped(MinecraftServer server) {}
}
