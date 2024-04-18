package fi.dy.masa.malilib;

import java.io.File;
import net.fabricmc.loader.api.FabricLoader;

public class MaLiLibReference
{
    public static final String MOD_ID = "malilib";
    public static final String MOD_NAME = "MaLiLib";
    public static final String MOD_VERSION = MaLiLib.getModVersionString(MOD_ID);

    public static final File DEFAULT_RUN_DIR = FabricLoader.getInstance().getGameDir().toFile();
    public static final File DEFAULT_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();
}
