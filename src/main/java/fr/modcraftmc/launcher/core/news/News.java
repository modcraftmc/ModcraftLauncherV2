package fr.modcraftmc.launcher.core.news;

import java.util.Date;

public class News {

    public String title;

    public String backgroundurl;

    public String text;

    public Date date;

    public News(String title, String backgroundurl, String text, Date date) {
        this.title = title;
        this.backgroundurl = backgroundurl;
        this.text = text;
        this.date = date;
    }
}
