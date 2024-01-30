package fi.dy.masa.malilib.event;

import fi.dy.masa.malilib.interfaces.ISyncmaticaPayloadListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SyncmaticaPayloadHandler implements ISyncmaticaPayloadManager
{
    private static final SyncmaticaPayloadHandler INSTANCE = new SyncmaticaPayloadHandler();
    private final List<ISyncmaticaPayloadListener> handlers = new ArrayList<>();
    public static ISyncmaticaPayloadManager getInstance() { return INSTANCE; }
    @Override
    public void registerSyncmaticaHandler(ISyncmaticaPayloadListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }
    @Override
    public void unregisterSyncmaticaHandler(ISyncmaticaPayloadListener handler)
    {
        this.handlers.remove(handler);
    }

    /**
     * NOT PUBLIC API - DO NOT CALL
     */
    public void receiveSyncmaticaPayload(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (ISyncmaticaPayloadListener handler : this.handlers)
            {
                handler.receiveSyncmaticaPayload(data, ctx, id);
            }
        }
    }
    public void sendSyncmaticaPayload(NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            for (ISyncmaticaPayloadListener handler : this.handlers)
            {
                handler.sendSyncmaticaPayload(data);
            }
        }
    }
    public void encodeSyncmaticaPayload(NbtCompound data, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (ISyncmaticaPayloadListener handler : this.handlers)
            {
                handler.encodeSyncmaticaPayload(data, id);
            }
        }
    }
    public void decodeSyncmaticaPayload(NbtCompound data, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (ISyncmaticaPayloadListener handler : this.handlers)
            {
                handler.decodeSyncmaticaPayload(data, id);
            }
        }
    }
}
