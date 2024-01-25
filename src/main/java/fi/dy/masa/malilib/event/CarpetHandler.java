package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ICarpetListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class CarpetHandler implements ICarpetManager
{
    private static final CarpetHandler INSTANCE = new CarpetHandler();
    private final List<ICarpetListener> handlers = new ArrayList<>();
    public static ICarpetManager getInstance() { return INSTANCE; }
    @Override
    public void registerCarpetHandler(ICarpetListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }
    @Override
    public void unregisterCarpetHandler(ICarpetListener handler)
    {
        this.handlers.remove(handler);
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void onCarpetPayload(NbtCompound data, ClientPlayNetworking.Context ctx)
    {
        if (!this.handlers.isEmpty())
        {
            for (ICarpetListener handler : this.handlers)
            {
                handler.onCarpetPayload(data, ctx);
            }
        }
    }
}
