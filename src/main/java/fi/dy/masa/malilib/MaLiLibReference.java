package fi.dy.masa.malilib;

import java.io.File;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import fi.dy.masa.malilib.util.StringUtils;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    public static final String MOD_VERSION = StringUtils.getModVersionString(MOD_ID);
    public static final String MC_VERSION = MinecraftVersion.CURRENT.getName();
    public static final String MOD_TYPE = "fabric";
    public static final String MOD_STRING = MOD_ID+"-"+MOD_TYPE+"-"+MC_VERSION+"-"+MOD_VERSION;

    public static final File DEFAULT_RUN_DIR = FabricLoader.getInstance().getGameDir().toFile();
    public static final File DEFAULT_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();

    private static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isServer() { return MOD_ENVIRONMENT == EnvType.SERVER; }
}
