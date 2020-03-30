package fr.modcraftmc.launcher.core.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourcesManager {

    private List<String> imagesExtentions = Arrays.asList(".png", ".gif", ".jpg", ".jpeg");

    private List<String> fxmlExtentions = Arrays.asList(".fxml");

    public ResourcesManager() {

    }

    public Object getResource(String name) {

        int lastIndexOf = name.lastIndexOf(".");
        String extention = name.substring(lastIndexOf);
        System.out.println(extention);

        if (imagesExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("images/" + name);
        } else if (fxmlExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("fxml/" + name);
        }

        return null;
    }
}
