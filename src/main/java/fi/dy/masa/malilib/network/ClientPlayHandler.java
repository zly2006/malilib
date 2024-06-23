package fi.dy.masa.malilib.network;

import com.google.common.collect.ArrayListMultimap;
import org.jetbrains.annotations.ApiStatus;
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
        Identifier channel = handler.getPayloadChannel();

        if (this.handlers.containsEntry(channel, handler) == false)
        {
            this.handlers.put(channel, (IPluginClientPlayHandler<T>) handler);
        }
    }

    @Override
    public <P extends CustomPayload> void unregisterClientPlayHandler(IPluginClientPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();

        if (this.handlers.remove(channel, handler))
        {
            handler.reset(channel);
            handler.unregisterPlayReceiver();
        }
    }

    @ApiStatus.Internal
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
     * This allows your Data Channel to be "shared" among more than one mod.
     * Using the IClientPayloadData interface as the Data Packet type.
     * @param channel (The shared Channel)
     * @param data (The Data type packet)
     * @param <P> (The Type of Data as a Generic)
     */
    @ApiStatus.Internal
    public <P extends IClientPayloadData> void decodeServerData(Identifier channel, P data)
    {
        if (this.handlers.isEmpty() == false)
        {
            for (IPluginClientPlayHandler<T> handler : this.handlers.get(channel))
            {
                handler.decodeClientData(channel, data);
            }
        }
    }
}
