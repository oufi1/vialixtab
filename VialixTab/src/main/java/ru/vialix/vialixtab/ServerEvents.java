package ru.vialix.vialixtab;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.vialix.vialixtab.network.NetworkHandler;
import ru.vialix.vialixtab.network.TpsSyncPacket;

@Mod.EventBusSubscriber(modid = VialixTab.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ServerEvents {
    private static final int SYNC_EVERY = 40; // раз в 2 секунды (20 тиков = 1 секунда)
    private static int tick = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tick++;
        if (tick >= SYNC_EVERY) {
            tick = 0;
            MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
            double averageTickTime = server.getAverageTickTime();
            float tps = (float) Math.min(20.0, 1000.0 / (averageTickTime > 0 ? averageTickTime : 50));
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                NetworkHandler.INSTANCE.sendTo(
                        new TpsSyncPacket(tps),
                        player.connection.connection,
                        net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT
                );
            }
        }
    }
}