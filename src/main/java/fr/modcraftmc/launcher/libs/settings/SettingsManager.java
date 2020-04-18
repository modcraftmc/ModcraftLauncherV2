package fr.modcraftmc.launcher.libs.settings;

import com.google.gson.Gson;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.discord.DiscordIntegration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

    private Gson gson;
    private DiscordIntegration discordIntegration;

    private Settings setting = new Settings();

    public SettingsManager() {
        gson = new Gson();

        try {
            setting = gson.fromJson(new FileReader(ModcraftLauncher.filesManager.getOptionsPath()), Settings.class);
            System.out.println(setting);
            System.out.println(setting.useDiscordRPC());
        } catch (Exception e) {
            ModcraftLauncher.LOGGER.warning("Error while loading options.");
            e.printStackTrace();
        }

        discordIntegration = new DiscordIntegration();
        //discordIntegration.setState(setting.useDiscordRPC());
    }

    public void save() {

        try {
            FileWriter writer = new FileWriter(ModcraftLauncher.filesManager.getOptionsPath());
            writer.write(gson.toJson(setting));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings getSetting() {
        return setting;
    }
}
