package ru.vialix.vialixtab;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = VialixTab.MODID, value = Dist.CLIENT)
public class ClientEvents {

    // Отключаем стандартный TAB (ванильный player list)
    @SubscribeEvent
    public static void onTabPre(RenderGuiOverlayEvent.Pre event) {
        // id() возвращает ResourceLocation, сравниваем с "minecraft:player_list"
        if (event.getOverlay().id().toString().equals("minecraft:player_list")) {
            event.setCanceled(true);
        }
    }

    // Рисуем кастомный TAB после того как ванильный отменён
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return;

        // Показываем оверлей только когда TAB (keyPlayerList) нажат и нет открытых экранов
        if (mc.screen == null && mc.options.keyPlayerList.isDown()) {
            GuiGraphics gui = event.getGuiGraphics();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            // Панель
            int panelWidth = 220;
            List<String> players = mc.player.connection.getOnlinePlayers().stream()
                    .map(p -> p.getProfile().getName())
                    .collect(Collectors.toList());
            int playerCount = players.size();
            int panelHeight = 40 + 16 * playerCount + 60;
            int x = (width - panelWidth) / 2;
            int y = 30;

            // Серый фон
            gui.fill(x, y, x + panelWidth, y + panelHeight, 0xCC333333);

            // Белая надпись "Vialix"
            String title = "Vialix";
            int titleY = y + 10;
            gui.drawCenteredString(mc.font, Component.literal(title), x + panelWidth/2, titleY, 0xFFFFFF);

            // Список игроков
            int listY = y + 35;
            for (String player : players) {
                gui.drawString(mc.font, Component.literal(player), x + 20, listY, 0xFFFFFF);
                listY += 16;
            }

            // --- Нижние пункты ---
            // TPS — фейковый, если нужно получать с сервера, потребуется отдельный sync
            int tps = 20;

            // Количество игроков и максимум (максимум на клиенте не узнать, подставь своё число)
            int online = players.size();
            int maxPlayers = 20;

            // Пинг игрока
            int ping = mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId()).getLatency();

            String tpsStr = "ТПС: " + tps;
            String playersStr = "ИГРОКИ: " + online + "/" + maxPlayers;
            String pingStr = "ПИНГ: " + ping;

            int infoY = y + panelHeight - 56;
            gui.drawString(mc.font, tpsStr, x + 20, infoY, 0xFFFFFF);
            gui.drawString(mc.font, playersStr, x + 20, infoY + 18, 0xFFFFFF);
            gui.drawString(mc.font, pingStr, x + 20, infoY + 36, 0xFFFFFF);
        }
    }
}