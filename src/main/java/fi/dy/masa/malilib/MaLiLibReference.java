package fi.dy.masa.malilib;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    // For keeping networking API separated
    public static final String MOD_VERSION = MaLiLib.getModVersionString(MOD_ID);
    public static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static final String COMMON_NAMESPACE = "fi.dy.masa";
    // Namespace For Network API
    public static boolean SINGLE_PLAYER = false;
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isSinglePlayer() { return SINGLE_PLAYER; }
}
