package ru.vialix.vialixtab;

import net.minecraftforge.fml.common.Mod;
import ru.vialix.vialixtab.network.NetworkHandler;

@Mod(VialixTab.MODID)
public class VialixTab {
    public static final String MODID = "vialixtab";

    public VialixTab() {
        // Регистрируем сетевые пакеты
        NetworkHandler.register();
        // Если нужен clientSetup, добавь метод в ClientEvents и раскомментируй ниже:
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEvents::clientSetup);
    }
}