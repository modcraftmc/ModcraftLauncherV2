package fr.modcraftmc.launcher.core.utils;

import com.google.gson.Gson;
import fr.modcraftmc.launcher.core.servers.Server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<Server> readJson(String URL, Type clasz)  {
        try {
            java.net.URL url = new URL(URL);
            InputStreamReader reader = new InputStreamReader(url.openStream());

            return new Gson().fromJson(reader, clasz);
        } catch (IOException ignored) {}

        return new ArrayList<>();
    }
}
