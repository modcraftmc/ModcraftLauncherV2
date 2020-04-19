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

    private Settings setting;

    public SettingsManager() {
        gson = new Gson();

        try {
            FileReader reader = new FileReader(ModcraftLauncher.filesManager.getOptionsPath());
            setting = gson.fromJson(reader, Settings.class);
            reader.close();
        } catch (Exception e) {
            ModcraftLauncher.LOGGER.warning("Error while loading options.");
            ModcraftLauncher.filesManager.getOptionsPath().deleteOnExit();
            setting = gson.fromJson("{\"accesToken\":\"\",\"keepLogin\":true,\"discordRPC\":true}", Settings.class);
            e.printStackTrace();
        }

        discordIntegration = new DiscordIntegration();
        discordIntegration.setState(setting.useDiscordRPC());
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
