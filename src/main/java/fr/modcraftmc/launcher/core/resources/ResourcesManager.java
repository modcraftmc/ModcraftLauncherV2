package fr.modcraftmc.launcher.core.resources;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ResourcesManager {

    private final List<String> imagesExtentions = Arrays.asList(".png", ".gif", ".jpg", ".jpeg");

    private final List<String> fxmlExtentions = Arrays.asList(".fxml");

    private final List<String> cssExtentions = Arrays.asList(".css");

    private final List<String> jsonExtentions = Arrays.asList(".json");


    public URL getResource(String name) {

        int lastIndexOf = name.lastIndexOf(".");
        String extention = name.substring(lastIndexOf);
        if (imagesExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("images/" + name);
        } else if (fxmlExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("fxml/" + name);
        } else if (cssExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("css/" + name);
        } else if (jsonExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("json" + name);
        } else {
            return getClass().getClassLoader().getResource(name);
        }

    }

    public InputStream getResourceAsStream(String name) {

        int lastIndexOf = name.lastIndexOf(".");
        String extention = name.substring(lastIndexOf);
        if (imagesExtentions.contains(extention)) {
            return getClass().getClassLoader().getResourceAsStream("images/" + name);
        } else if (fxmlExtentions.contains(extention)) {
            return getClass().getClassLoader().getResourceAsStream("fxml/" + name);
        } else if (cssExtentions.contains(extention)) {
            return getClass().getClassLoader().getResourceAsStream("css/" + name);
        } else if (jsonExtentions.contains(extention)) {
            return getClass().getClassLoader().getResourceAsStream("json" + name);
        } else {
            return getClass().getClassLoader().getResourceAsStream(name);
        }
    }
}
