package fi.dy.masa.malilib;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;

import java.io.File;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    public static final String MOD_VERSION = MaLiLib.getModVersionString(MOD_ID);
    public static final String MOD_TYPE = "fabric";
    public static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static final File RUN_DIR = FabricLoader.getInstance().getGameDir().toFile();
    public static final File CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();
    public static final String MC_VERSION = MinecraftVersion.CURRENT.getName();
    private static boolean dedicated_server = false;
    private static boolean integrated_server = false;
    private static boolean open_to_lan = false;
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isServer() { return MOD_ENVIRONMENT == EnvType.SERVER; }
    public static boolean isDedicated() { return dedicated_server; }
    public static boolean isIntegrated() { return integrated_server; }
    public static boolean isOpenToLan() { return open_to_lan; }
    public static void setDedicated(boolean toggle)
    {
        if (toggle)
        {
            if (isServer())
            {
                dedicated_server = true;
            }
            else
                dedicated_server = false;
        }
        else
        {
            dedicated_server = false;
        }
        integrated_server = false;
        open_to_lan = false;
    }
    public static void setIntegrated(boolean toggle)
    {
        if (toggle)
        {
            if (isClient())
            {
                integrated_server = true;
            }
            else
            {
                integrated_server = false;
                open_to_lan = false;
            }
            dedicated_server = false;
        }
        else
        {
            dedicated_server = false;
            integrated_server = false;
            open_to_lan = false;
        }
    }
    public static void setOpenToLan(boolean toggle)
    {
        if (toggle)
        {
            if (isClient())
            {
                open_to_lan = true;
                integrated_server = true;
            }
            else
            {
                open_to_lan = false;
                integrated_server = false;
            }
            dedicated_server = false;
        }
        else
        {
            dedicated_server = false;
            integrated_server = false;
            open_to_lan = false;
        }
    }
    // For keeping networking API separated for basic sanity checks.
}
