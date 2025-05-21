package ru.vialix.vialixtab.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TpsSyncPacket {
    private final float tps;

    public TpsSyncPacket(float tps) {
        this.tps = tps;
    }

    public static void encode(TpsSyncPacket pkt, FriendlyByteBuf buf) {
        buf.writeFloat(pkt.tps);
    }

    public static TpsSyncPacket decode(FriendlyByteBuf buf) {
        return new TpsSyncPacket(buf.readFloat());
    }

    public static void handle(TpsSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ru.vialix.vialixtab.ClientTpsHolder.setTps(pkt.tps);
        });
        ctx.get().setPacketHandled(true);
    }
}