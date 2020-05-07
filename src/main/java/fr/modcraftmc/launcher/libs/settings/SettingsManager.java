package fr.modcraftmc.launcher.libs.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import fr.modcraftmc.launcher.core.ModcraftLauncher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

    private Settings settings;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public Settings load() {

         settings = new Settings("1", "null", true, true);

        try {
            JsonReader fileReader = new JsonReader(new FileReader(ModcraftLauncher.filesManager.getOptionsPath()));
            BufferedReader reader = new BufferedReader(new FileReader(ModcraftLauncher.filesManager.getOptionsPath()));
            if (reader.readLine() != null)
                settings = gson.fromJson(fileReader, Settings.class);
            else
                save();

            } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(settings.version);
        System.out.println(settings.accesToken);
        System.out.println(settings.discordRPC);
        System.out.println(settings.keepLogin);


        return settings;

    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(ModcraftLauncher.filesManager.getOptionsPath());
            writer.write(gson.toJson(settings));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Settings getSettings() {
        return settings;
    }
}
