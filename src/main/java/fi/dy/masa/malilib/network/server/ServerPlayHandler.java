package fi.dy.masa.malilib.network.server;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import fi.dy.masa.malilib.network.payload.MaLiLibByteBuf;
import fi.dy.masa.malilib.network.payload.PayloadType;

/**
 * The Server Network Play handler
 * @param <T> (Payload)
 */
public class ServerPlayHandler<T extends CustomPayload> implements IServerPlayHandler
{
    private static final ServerPlayHandler<CustomPayload> INSTANCE = new ServerPlayHandler<>();
    private final ArrayListMultimap<PayloadType, IPluginServerPlayHandler<T>> handlers = ArrayListMultimap.create();
    public static IServerPlayHandler getInstance()
    {
        return INSTANCE;
    }

    private ServerPlayHandler() { }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends CustomPayload> void registerServerPlayHandler(IPluginServerPlayHandler<P> handler)
    {
        PayloadType type = handler.getPayloadType();

        if (type.exists(type))
        {
            if (!this.handlers.containsEntry(type, handler))
            {
                this.handlers.put(type, (IPluginServerPlayHandler<T>) handler);
                handler.registerPlayPayload(type);
            }
        }
    }

    @Override
    public <P extends CustomPayload> void unregisterServerPlayHandler(IPluginServerPlayHandler<P> handler)
    {
        PayloadType type = handler.getPayloadType();

        if (type.exists(type))
        {
            if (this.handlers.remove(type, handler))
            {
                handler.unregisterPlayHandler(type);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void reset(PayloadType type)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
            {
                handler.reset(type);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void registerPlayPayload(PayloadType type)
    {
       if (!this.handlers.isEmpty())
       {
           for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
           {
               handler.registerPlayPayload(type);
           }
       }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void registerPlayHandler(PayloadType type)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
            {
                handler.registerPlayHandler(type);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void unregisterPlayHandler(PayloadType type)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
            {
                handler.unregisterPlayHandler(type);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void decodeC2SNbtCompound(PayloadType type, NbtCompound data, ServerPlayerEntity player)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
            {
                handler.decodeC2SNbtCompound(type, data, player);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void decodeC2SByteBuf(PayloadType type, MaLiLibByteBuf data, ServerPlayerEntity player)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginServerPlayHandler<T> handler : this.handlers.get(type))
            {
                handler.decodeC2SByteBuf(type, data, player);
            }
        }
    }
}
