package fi.dy.masa.malilib.network;

import net.minecraft.util.Identifier;

public interface IPayloadType
{
    PayloadType getType();
    String getKey();
    String getNamespace();
    String getPath();
    Identifier getId();
}
