package fr.modcraftmc.launcher.core.servers;

import com.google.gson.reflect.TypeToken;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.utils.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private static List<Server> serverList = new ArrayList<>();

    public static void init() {
        ModcraftLauncher.LOGGER.info("Fetching servers list...");

        Type typeOfT = TypeToken.getParameterized(List.class, Server.class).getType();
        serverList = JSONUtils.readJson("http://v1.modcraftmc.fr/api/servers/servers.json", typeOfT);
        ModcraftLauncher.LOGGER.info("Found {} servers", serverList.size());


    }
}
