package fr.modcraft.launcher.utils;

import com.google.gson.Gson;
import fr.modcraft.launcher.maintenance.Maintenance;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonUtils {

    public static Maintenance readJson(String URL) throws IOException {
        URL url = new URL(URL);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return new Gson().fromJson(reader, Maintenance.class);
    }
}
