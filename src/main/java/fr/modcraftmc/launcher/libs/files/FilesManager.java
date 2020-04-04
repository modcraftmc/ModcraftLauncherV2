package fr.modcraftmc.launcher.libs.files;

import java.io.File;
import java.io.IOException;

public class FilesManager {

    public static File DEFAULT_PATH = new File(System.getenv("appdata") + "\\.modcraftmc\\");
    public static File OPTIONS_PATH = new File(DEFAULT_PATH, "modcraftlauncher.json");
    public static File INSTANCES_PATH = new File(DEFAULT_PATH, "instances");

    public FilesManager() {
        try {
            if (!DEFAULT_PATH.exists()) {
                DEFAULT_PATH.mkdirs();
            }
            if (!OPTIONS_PATH.exists()) {
                OPTIONS_PATH.createNewFile();
            }
            if (!INSTANCES_PATH.exists()) {
                INSTANCES_PATH.mkdirs();
            }
        } catch (IOException e) {

            //TODO: create crash reporter
        }
    }

    public File getDefaultPath() {
        return DEFAULT_PATH;
    }

    public File getOptionsPath() {
        return OPTIONS_PATH;
    }

    public File getInstancesPath() {
        return INSTANCES_PATH;
    }

    public void setDefaultPath(File defaultPath) {
        DEFAULT_PATH = defaultPath;
    }

    public void setOptionsPath(File optionsPath) {
        OPTIONS_PATH = optionsPath;
    }

    public void setInstancesPath(File instancesPath) {
        INSTANCES_PATH = instancesPath;
    }
}
