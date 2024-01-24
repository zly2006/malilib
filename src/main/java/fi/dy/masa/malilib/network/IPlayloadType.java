package fi.dy.masa.malilib.network;

import net.minecraft.util.Identifier;

public interface IPlayloadType
{
    PayloadTypes.PayloadType getPayloadType();
    String getNamespace();
    String getPath();
    Identifier getIdentifier();
}
