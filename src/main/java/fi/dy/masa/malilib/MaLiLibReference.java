package fi.dy.masa.malilib;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.registry.DynamicRegistryManager;

import java.io.File;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    public static final String MOD_VERSION = MaLiLib.getModVersionString(MOD_ID);
    public static final String MC_VERSION = MinecraftVersion.CURRENT.getName();
    public static final String MOD_TYPE = "fabric";
    public static final String MOD_STRING = MOD_ID+"-"+MOD_TYPE+"-"+MC_VERSION+"-"+MOD_VERSION;
    public static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static final File DEFAULT_RUN_DIR = FabricLoader.getInstance().getGameDir().toFile();
    public static final File DEFAULT_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();
    /**
     * There is probably a "cleaner" way to manage this data,
     * Many parts of the new Network API depend upon these being set correctly,
     * and this helps MaLiLib maintain it's Multi-Environment status correctly.
     */
    private static boolean dedicated_server = false;
    private static boolean integrated_server = false;
    private static boolean open_to_lan = false;
    private static boolean hasCarpetClient = false;
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isServer() { return MOD_ENVIRONMENT == EnvType.SERVER; }
    public static boolean isDedicated() { return dedicated_server; }
    public static boolean isIntegrated() { return integrated_server; }
    public static boolean isOpenToLan() { return open_to_lan; }
    public static boolean hasCarpetClient() { return hasCarpetClient; }
    private static DynamicRegistryManager registryManager = DynamicRegistryManager.EMPTY;

    public static void setDedicated(boolean toggle)
    {
        if (toggle && isServer())
        {
            dedicated_server = true;
            integrated_server = false;
            open_to_lan = false;
        }
        else
        {
            dedicated_server = false;
        }
    }

    public static void setIntegrated(boolean toggle)
    {
        if (toggle && isClient())
        {
            integrated_server = true;
            open_to_lan = false;
            dedicated_server = false;
        }
        else
        {
            integrated_server = false;
        }
    }

    public static void setOpenToLan(boolean toggle)
    {
        if (toggle && isClient())
        {
            open_to_lan = true;
            integrated_server = true;
            dedicated_server = false;
        }
        else
        {
            open_to_lan = false;
        }
    }

    // For keeping networking API separated for basic sanity checks.
    public static void setCarpetClient(boolean toggle)
    {
        if (toggle && isClient())
        {
            hasCarpetClient = true;
        }
        else
        {
            hasCarpetClient = false;
        }
    }

    public static DynamicRegistryManager getRegistryManager()
    {
        return registryManager;
    }

    public static void setRegistryManager(DynamicRegistryManager manager)
    {
        if (manager != DynamicRegistryManager.EMPTY)
        {
            registryManager = manager;
        }
    }
}
