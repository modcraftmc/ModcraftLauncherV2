package fr.modcraftmc.launcher.core.news;

import com.google.gson.reflect.TypeToken;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.utils.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewsManager {

    private static List<News> newsList = new ArrayList<>();
    private static final Timer timer = new Timer();
    private static final Type typeOfT = TypeToken.getParameterized(List.class, News.class).getType();

    public static void init() {
        ModcraftLauncher.LOGGER.info("Fetching news...");

        newsList = JSONUtils.fetchNews("http://v1.modcraftmc.fr/api/news/news.json", typeOfT);
        ModcraftLauncher.LOGGER.info("News found : " + newsList.size());

        refresh();

    }

    public static void refresh() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                newsList = JSONUtils.fetchNews("http://v1.modcraftmc.fr/api/news/news.json", typeOfT);

            }
        }, 60000, 60000);

    }

    public static List<News> getNewsList() {
        return newsList;
    }
}
