package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ICarpetHelloListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class CarpetHelloHandler implements ICarpetHelloManager
{
    private static final CarpetHelloHandler INSTANCE = new CarpetHelloHandler();
    private final List<ICarpetHelloListener> handlers = new ArrayList<>();
    public static ICarpetHelloManager getInstance() { return INSTANCE; }
    @Override
    public void registerCarpetHelloHandler(ICarpetHelloListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }
    @Override
    public void unregisterCarpetHelloHandler(ICarpetHelloListener handler)
    {
        this.handlers.remove(handler);
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void sendCarpetHello(NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            for (ICarpetHelloListener handler : this.handlers)
            {
                handler.sendCarpetHello(data);
            }
        }
    }
    public void receiveCarpetHello(NbtCompound data, ClientPlayNetworking.Context ctx)
    {
        if (!this.handlers.isEmpty())
        {
            for (ICarpetHelloListener handler : this.handlers)
            {
                handler.receiveCarpetHello(data, ctx);
            }
        }
    }
}
