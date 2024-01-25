package fi.dy.masa.malilib.network;

import net.minecraft.util.Identifier;

import java.util.Objects;

public class PayloadTypes implements IPayloadType
{
    public enum PayloadType {
        C2S_STRING,
        S2C_STRING,
        C2S_DATA,
        S2C_DATA,
        CARPET_HELLO
    }
    private final PayloadType type;
    private String path;
    private final String name;
    private final Identifier id;

    public PayloadTypes(PayloadType type, String namespace)
    {
        this.type = type;
        this.name = namespace;
        this.setType();
        this.id = new Identifier(this.name, this.path);
    }
    public Identifier getIdentifier()
    {
        if (checkValidType())
            return this.id;
        else return null;
    }
    private void setType()
    {
        switch (this.type) {
            case C2S_DATA -> this.path = "c2s-data";
            case S2C_DATA -> this.path = "s2c-data";
            case C2S_STRING -> this.path = "c2s-string";
            case S2C_STRING -> this.path = "s2c-string";
            case CARPET_HELLO -> this.path = "hello";
            default -> this.path = "invalid";
        }
    }
    private boolean checkValidType()
    {
        return !Objects.equals(this.path, "invalid");
    }
}
