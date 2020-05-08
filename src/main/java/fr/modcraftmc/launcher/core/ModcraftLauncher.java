package fr.modcraftmc.launcher.core;

import fr.modcraftmc.FilesManager;
import fr.modcraftmc.launcher.core.resources.ResourcesManager;
import fr.modcraftmc.launcher.core.servers.ServerManager;
import fr.modcraftmc.launcher.libs.discord.DiscordIntegration;
import fr.modcraftmc.launcher.libs.serverpinger.ServerPingerThread;
import fr.modcraftmc.launcher.libs.settings.SettingsManager;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.application.Application;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

public class ModcraftLauncher {

    public final static Logger LOGGER = LoggerFactory.getLogger("ModcraftMC");

    public final static String OS_NAME = System.getProperty("os.name");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static FilesManager filesManager = new FilesManager();
    public final static SettingsManager settingsManager = new SettingsManager();
    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static ServerPingerThread serverPingerThread = new ServerPingerThread();


    public static void main(String[] args) {

        settingsManager.load();

        LOGGER.info("Starting ModcraftLauncher");
        LOGGER.info("By Modcraftmc developpement team");


        new Thread(serverPingerThread).start();
        if (settingsManager.getSettings().discordRPC) new DiscordIntegration();

        ServerManager.init();

        Application.launch(ModcraftApplication.class, args);

    }

}
