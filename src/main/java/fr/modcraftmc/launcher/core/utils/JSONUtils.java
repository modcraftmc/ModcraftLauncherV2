package fr.modcraftmc.launcher.core.utils;

import com.google.gson.Gson;
import fr.modcraftmc.launcher.core.servers.Server;
import fr.modcraftmc.launcher.libs.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<Server> readJson(String URL, Type clazz)  {
        try {
            java.net.URL url = new URL(URL);
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return new Gson().fromJson(reader, clazz);
        } catch (IOException ignored) {}

        return new ArrayList<>();
    }

    public static Settings readSettings(File file, Type clazz)  {
        try {
            java.net.URL url = file.toURL();
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return new Gson().fromJson(reader, clazz);
        } catch (IOException ignored) {}

        return new Settings("1", "oh no no no no", true, true, "FR_fr");
    }
}
