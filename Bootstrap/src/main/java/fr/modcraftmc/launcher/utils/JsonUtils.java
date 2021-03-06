package fr.modcraftmc.launcher.utils;

import com.google.gson.Gson;
import fr.modcraftmc.launcher.maintenance.Maintenance;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

public class JsonUtils {

    public static Maintenance readJson(Type clazz,String URL) throws IOException {
        URL url = new URL(URL);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return new Gson().fromJson(reader, clazz);
    }
}
