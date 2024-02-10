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
import fi.dy.masa.malilib.network.ClientNetworkPlayInitHandler;
import fi.dy.masa.malilib.network.packet.PacketUtils;

public class MaLiLibInitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(MaLiLibReference.MOD_ID, new MaLiLibConfigs());
        InputEventHandler.getKeybindManager().registerKeybindProvider(MaLiLibInputHandler.getInstance());

        MaLiLibConfigs.Generic.OPEN_GUI_CONFIGS.getKeybind().setCallback(new CallbackOpenConfigGui());

        // Register network protocols
        ClientNetworkPlayInitHandler.registerPlayChannels();
        ServerListener serverListener = new ServerListener();
        ServerHandler.getInstance().registerServerHandler(serverListener);
        PacketUtils.registerPayloads();
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
