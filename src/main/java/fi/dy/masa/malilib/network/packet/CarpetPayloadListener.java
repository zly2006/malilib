package fi.dy.masa.malilib.network.packet;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.interfaces.ICarpetListener;
import fi.dy.masa.malilib.network.ClientNetworkPlayHandler;
import fi.dy.masa.malilib.network.payload.CarpetPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;

public class CarpetPayloadListener implements ICarpetListener
{
    final String CARPET_HI = "69";
    final String CARPET_HELLO = "420";
    @Override
    public void receiveCarpetPayload(NbtCompound data, ClientPlayNetworking.Context ctx)
    {
        String carpetVersion = data.getString(CARPET_HI);
        MaLiLib.printDebug("ICarpetListener#onCarpetPayload(): received Carpet Hello packet. (Carpet Server {})", carpetVersion);
        if (!MaLiLibReference.hasCarpetServer())
            MaLiLibReference.CARPET_CLIENT = true;

        // Send Hello packet back to server, tell them that this is MaLiLib :)
        NbtCompound response = new NbtCompound();
        response.putString(CARPET_HELLO, MaLiLibReference.MOD_ID+"-"+MaLiLibReference.MOD_VERSION);
        sendCarpetPayload(response);
    }
    @Override
    public void sendCarpetPayload(NbtCompound data)
    {
        CarpetPayload payload = new CarpetPayload(data);
        ClientNetworkPlayHandler.sendCarpet(payload);
    }
}
