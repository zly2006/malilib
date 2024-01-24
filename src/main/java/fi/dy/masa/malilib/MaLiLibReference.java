package fi.dy.masa.malilib;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    // For keeping networking API separated
    public static final EnvType MOD_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();
    public static boolean SINGLE_PLAYER = true;
    public static boolean isClient() { return MOD_ENVIRONMENT == EnvType.CLIENT; }
    public static boolean isServer() { return MOD_ENVIRONMENT == EnvType.SERVER; }
    public static boolean isSinglePlayer() { return SINGLE_PLAYER; }
}
