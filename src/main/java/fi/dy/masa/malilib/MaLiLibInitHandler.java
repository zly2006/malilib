package fi.dy.masa.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.CarpetHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.network.events.CarpetPayloadListener;

public class MaLiLibInitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(MaLiLibReference.MOD_ID, new MaLiLibConfigs());
        InputEventHandler.getKeybindManager().registerKeybindProvider(MaLiLibInputHandler.getInstance());

        MaLiLibConfigs.Generic.OPEN_GUI_CONFIGS.getKeybind().setCallback(new CallbackOpenConfigGui());

        // Register Network Listeners
        CarpetPayloadListener listener = new CarpetPayloadListener();
        CarpetHandler.getInstance().registerCarpetHandler(listener);
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
