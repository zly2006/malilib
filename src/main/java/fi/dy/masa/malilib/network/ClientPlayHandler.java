package fi.dy.masa.malilib.network;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * The Client Network Play handler
 * @param <T> (Payload)
 */
public class ClientPlayHandler<T extends CustomPayload> implements IClientPlayHandler
{
    private static final ClientPlayHandler<CustomPayload> INSTANCE = new ClientPlayHandler<>();
    private final ArrayListMultimap<Identifier, IPluginClientPlayHandler<T>> handlers = ArrayListMultimap.create();
    public static IClientPlayHandler getInstance()
    {
        return INSTANCE;
    }

    private ClientPlayHandler() {}

    @Override
    @SuppressWarnings("unchecked")
    public <P extends CustomPayload> void registerClientPlayHandler(IPluginClientPlayHandler<P> handler)
    {
        boolean isRegistered = this.isClientPlayChannelRegistered(handler);
        Identifier channel = handler.getPayloadChannel();

        if (this.handlers.containsEntry(channel, handler) == false)
        {
            this.handlers.put(channel, (IPluginClientPlayHandler<T>) handler);

            if (handler.isPlayRegistered(channel) == false && isRegistered == false)
            {
                handler.registerPlayPayload(channel);
            }

            handler.setPlayRegistered(channel);
        }
    }

    @Override
    public <P extends CustomPayload> boolean isClientPlayChannelRegistered(IPluginClientPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();
        boolean isRegistered = false;

        for (IPluginClientPlayHandler<T> handlerEnt : this.handlers.get(channel))
        {
            if (isRegistered == false)
            {
                isRegistered = handlerEnt.isPlayRegistered(channel);
            }
        }

        return isRegistered;
    }

    @Override
    public <P extends CustomPayload> void unregisterClientPlayHandler(IPluginClientPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();

        if (this.handlers.remove(channel, handler))
        {
            handler.unregisterPlayHandler(channel);
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void reset(Identifier channel)
    {
        if (this.handlers.isEmpty() == false)
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.reset(channel);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void registerPlayPayload(Identifier channel)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.registerPlayPayload(channel);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void registerPlayHandler(Identifier channel)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.registerPlayHandler(channel);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void unregisterPlayHandler(Identifier channel)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.unregisterPlayHandler(channel);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void decodeNbtCompound(Identifier channel, NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.decodeNbtCompound(channel, data);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void decodeByteBuf(Identifier channel, MaLiLibBuf data)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.decodeByteBuf(channel, data);
            }
        }
    }

    /**
     * API CALLS DO NOT USE ANYWHERE ELSE (DANGEROUS!)
     */
    public void decodeObject(Identifier channel, Object data)
    {
        if (!this.handlers.isEmpty())
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.decodeObject(channel, data);
            }
        }
    }
}
