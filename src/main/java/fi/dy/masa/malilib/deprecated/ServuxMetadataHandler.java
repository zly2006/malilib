package fi.dy.masa.malilib.deprecated;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface Handler for Servux Metadata packets (Any downstream mod who needs special server-side data)
 */
@Deprecated
public class ServuxMetadataHandler implements IServuxMetadataManager
{
    private static final ServuxMetadataHandler INSTANCE = new ServuxMetadataHandler();
    private final List<IServuxMetadataListener> handlers = new ArrayList<>();
    public static IServuxMetadataManager getInstance() { return INSTANCE; }
    @Override
    public void registerServuxMetadataHandler(IServuxMetadataListener handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }
    @Override
    public void unregisterServuxMetadataHandler(IServuxMetadataListener handler)
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
            for (IServuxMetadataListener handler : this.handlers)
            {
                handler.reset();
            }
        }
    }
    public void receiveServuxMetadata(NbtCompound data, ClientPlayNetworking.Context ctx, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxMetadataListener handler : this.handlers)
            {
                handler.receiveServuxMetadata(data, ctx, id);
            }
        }
    }
    public void decodeServuxMetadata(NbtCompound data, Identifier id)
    {
        if (!this.handlers.isEmpty())
        {
            for (IServuxMetadataListener handler : this.handlers)
            {
                handler.decodeServuxMetadata(data, id);
            }
        }
    }
    public void sendServuxMetadata(NbtCompound data)
    {
        // Downstream mods should implement this
    }
    public void encodeServuxMetadata(NbtCompound data, Identifier id)
    {
        // Downstream mods should implement this
    }
}
