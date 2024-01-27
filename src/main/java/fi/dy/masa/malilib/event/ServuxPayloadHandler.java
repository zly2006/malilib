package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.IServuxPayloadListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ServuxPayloadHandler implements IServuxPayloadManager
{
    private static final ServuxPayloadHandler INSTANCE = new ServuxPayloadHandler();
    private final List<IServuxPayloadListener> handlers = new ArrayList<>();
    public static IServuxPayloadManager getInstance() { return INSTANCE; }
    @Override
    public void registerServuxHandler(IServuxPayloadListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }
    @Override
    public void unregisterServuxHandler(IServuxPayloadListener handler)
    {
        this.handlers.remove(handler);
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void reset()
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxPayloadListener handler : this.handlers)
            {
                handler.reset();
            }
        }
    }
    public void receiveServuxPayload(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxPayloadListener handler : this.handlers)
            {
                handler.receiveServuxPayload(data, ctx, id);
            }
        }
    }

    public void sendServuxPayload(NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxPayloadListener handler : this.handlers)
            {
                handler.sendServuxPayload(data);
            }
        }
    }
    public void encodeServuxPayload(NbtCompound data, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxPayloadListener handler : this.handlers)
            {
                handler.encodeServuxPayload(data, id);
            }
        }
    }
    public void decodeServuxPayload(NbtCompound data, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxPayloadListener handler : this.handlers)
            {
                handler.decodeServuxPayload(data, id);
            }
        }
    }
}
