package fi.dy.masa.malilib.network;

import net.minecraft.util.Identifier;

import java.util.Objects;

public class PayloadTypes implements IPlayloadType
{
    private final PayloadType type;
    private final String path;
    private final String namespace;
    private final Identifier id;

    public PayloadTypes(PayloadType type, String namespace)
    {
        this.type = type;
        this.namespace = namespace;
        switch (type) {
            case C2S_DATA -> this.path = "c2s-data";
            case S2C_DATA -> this.path = "s2c-data";
            case C2S_STRING -> this.path = "c2s-string";
            case S2C_STRING -> this.path = "s2c-string";
            default -> this.path = "invalid";
        }
        this.id = new Identifier(this.namespace, this.path);
    }

    public PayloadType getPayloadType()
    {
        if (checkValidType())
            return this.type;
        else return null;
    }
    public String getNamespace() { return this.namespace; }
    public String getPath()
    {
        if (checkValidType())
            return this.path;
        else return null;
    }
    public Identifier getIdentifier()
    {
        if (checkValidType())
            return this.id;
        else return null;
    }

    private boolean checkValidType()
    {
        return !Objects.equals(this.path, "invalid");
    }
    public enum PayloadType {
        C2S_STRING,
        S2C_STRING,
        C2S_DATA,
        S2C_DATA;
    }
}
