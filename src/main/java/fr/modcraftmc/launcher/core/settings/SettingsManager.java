package fr.modcraftmc.launcher.core.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.utils.JSONUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

    private Settings settings = new Settings("1", "oh no no no no", true, true, true, "FR_fr", 4);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void load() {

            try {
                FileReader fileReader = new FileReader(ModcraftLauncher.filesManager.getOptionsPath());
                BufferedReader reader = new BufferedReader(fileReader);
                try {
                    if (reader.readLine() != null)
                        settings = JSONUtils.readSettings(ModcraftLauncher.filesManager.getOptionsPath(), Settings.class);
                    else
                        save();
                } catch (JsonIOException | JsonSyntaxException e) {
                    e.printStackTrace();
                    save();

                }

            } catch (Exception e) {
                //TODO: crash reporter
                e.printStackTrace();
            }

    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(ModcraftLauncher.filesManager.getOptionsPath());
            writer.write(gson.toJson(settings));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            //TODO: crash reporter
            e.printStackTrace();
        }
    }

    public Settings getSettings() {
        return settings;
    }
}
