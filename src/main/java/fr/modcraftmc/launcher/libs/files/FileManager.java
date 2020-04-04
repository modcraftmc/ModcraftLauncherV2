package fr.modcraftmc.launcher.libs.files;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static File DEFAULT_PATH = new File(System.getenv("appdata") + "\\.modcraftmc\\");
    public static File OPTIONS_PATH = new File(DEFAULT_PATH, "modcraftlauncher.json");
    public static File INSTANCES_PATH = new File(DEFAULT_PATH, "instances");

    public FileManager() {
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
}
