package fr.modcraftmc.launcher.libs.settings;

import fr.modcraftmc.launcher.libs.settings.impl.DiscordIntegration;

import java.util.ArrayList;

public class SettingsManager {

    public static ArrayList<ISetting> settingsList = new ArrayList<ISetting>();

    public SettingsManager() {

        settingsList.add(new DiscordIntegration());

    }

    public ISetting getSetting(Class clasz) {
        return settingsList.stream().filter(module -> module.getClass().equals(clasz)).findFirst().orElse(null);
    }

    public ISetting getSetting(String name) {
        return settingsList.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

    }

}
