package fi.dy.masa.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.ServerHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.listeners.ServerListener;
import fi.dy.masa.malilib.network.packet.PacketUtils_example;
import fi.dy.masa.malilib.util.FileUtils;

public class MaLiLibInitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        // Set Directories for Server Compatibility
        FileUtils.setRunDirectory(MaLiLibReference.RUN_DIR);
        FileUtils.setConfigDirectory(MaLiLibReference.CONFIG_DIR);
        ConfigManager.getInstance().registerConfigHandler(MaLiLibReference.MOD_ID, new MaLiLibConfigs());

        if (MaLiLibReference.isClient())
        {
            MaLiLib.logger.info("{}-{}-{}-{} --> Initializing CLIENT environment", MaLiLibReference.MOD_ID, MaLiLibReference.MOD_TYPE, MaLiLibReference.MC_VERSION, MaLiLibReference.MOD_VERSION);
            MaLiLib.getCarpetClient();

            InputEventHandler.getKeybindManager().registerKeybindProvider(MaLiLibInputHandler.getInstance());

            MaLiLibConfigs.Generic.OPEN_GUI_CONFIGS.getKeybind().setCallback(new CallbackOpenConfigGui());
        }
        if (MaLiLibReference.isServer())
        {
            MaLiLib.logger.info("{}-{}-{}-{} --> Initializing SERVER environment", MaLiLibReference.MOD_ID, MaLiLibReference.MOD_TYPE, MaLiLibReference.MC_VERSION, MaLiLibReference.MOD_VERSION);
        }
        // Used to register Payloads / MaLiLib Networking API under MinecraftServer status events
        ServerListener serverListener = new ServerListener();
        ServerHandler.getInstance().registerServerHandler(serverListener);

        // Example code.
        PacketUtils_example.registerPayloads();
    }

    private static class CallbackOpenConfigGui implements IHotkeyCallback
    {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            GuiBase.openGui(new MaLiLibConfigGui());
            return true;
        }
    }
}
