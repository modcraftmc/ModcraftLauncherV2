package fr.modcraftmc.launcher.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.modcraftmc.launcher.core.news.News;
import fr.modcraftmc.launcher.core.servers.Server;
import fr.modcraftmc.launcher.core.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static List<Server> fetchServersList(String URL, Type clazz)  {
        try {
            java.net.URL url = new URL(URL);
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return gson.fromJson(reader, clazz);
        } catch (IOException ignored) {}

        return new ArrayList<>();
    }

    public static Settings readSettings(File file, Type clazz)  {
        try {
            java.net.URL url = file.toURL();
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return gson.fromJson(reader, clazz);
        } catch (IOException ignored) {}

        return new Settings("1", "oh no no no no", true, true, true,"FR_fr", 4);
    }

    public static List<News> fetchNews(String URL, Type clazz)  {
        try {
            java.net.URL url = new URL(URL);
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return gson.fromJson(reader, clazz);
        } catch (IOException ignored) {}

        return new ArrayList<>();
    }
}
