package fr.modcraftmc.launcher.core.resources;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ResourcesManager {

    private List<String> imagesExtentions = Arrays.asList(".png", ".gif", ".jpg", ".jpeg");

    private List<String> fxmlExtentions = Arrays.asList(".fxml");

    private List<String> cssExtentions = Arrays.asList(".css");

    public ResourcesManager() {

    }

    public URL getResource(String name) {

        int lastIndexOf = name.lastIndexOf(".");
        String extention = name.substring(lastIndexOf);
        if (imagesExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("images/" + name);
        } else if (fxmlExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("fxml/" + name);
        } else if (cssExtentions.contains(extention)) {
            return getClass().getClassLoader().getResource("css/" + name);
        } else {
            return getClass().getClassLoader().getResource(name);
        }


/**
        try {
            throw new ResourceNotFoundException("resource not found : " + name);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }


        return null;
 **/
    }
}
