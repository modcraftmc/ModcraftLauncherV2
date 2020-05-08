package fr.modcraftmc.launcher.core.servers;

import com.google.gson.reflect.TypeToken;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.utils.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager {

    private static List<Server> serverList = new ArrayList<>();
    private static final Timer timer = new Timer();
    private static final Type typeOfT = TypeToken.getParameterized(List.class, Server.class).getType();

    public static void init() {
        ModcraftLauncher.LOGGER.info("Fetching servers list...");

        serverList = JSONUtils.fetchServersList("http://v1.modcraftmc.fr/api/servers/servers.json", typeOfT);
        ModcraftLauncher.LOGGER.info("Servers found : " + serverList.size());

        refresh();

    }

    public static void refresh() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                serverList = JSONUtils.fetchServersList("http://v1.modcraftmc.fr/api/servers/servers.json", typeOfT);

            }
        }, 60000, 60000);

    }

    public static List<Server> getServerList() {
        return serverList;
    }
}
