package fr.modcraftmc.launcher.libs.files;

import com.google.gson.Gson;
import fr.modcraftmc.launcher.libs.settings.Settings;

import java.io.File;
import java.io.FileWriter;

public class FilesManager {

    public static File DEFAULT_PATH = new File(System.getenv("appdata") + "\\.modcraftmc\\");
    public static File OPTIONS_PATH = new File(DEFAULT_PATH, "modcraftlauncher.json");
    public static File INSTANCES_PATH = new File(DEFAULT_PATH, "instances");
    public static File JAVA_PATH = new File(DEFAULT_PATH, "java");

    public FilesManager() {
        try {
            if (!DEFAULT_PATH.exists()) {
                DEFAULT_PATH.mkdirs();
            }
            if (!OPTIONS_PATH.exists()) {
                OPTIONS_PATH.createNewFile();
                Gson gson = new Gson();
                FileWriter fileWriter = new FileWriter(OPTIONS_PATH);
                fileWriter.write(gson.toJson(new Settings("", true)));
                fileWriter.flush();
                fileWriter.close();
            }
            if (!INSTANCES_PATH.exists()) {
                INSTANCES_PATH.mkdirs();
            }
            if (!JAVA_PATH.exists()) {
                JAVA_PATH.mkdirs();
            }
        } catch (Exception e) {
            OPTIONS_PATH.delete();
            e.printStackTrace();
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

    public static File getJavaPath() {
        return JAVA_PATH;
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

    public static void setJavaPath(File javaPath) {
        JAVA_PATH = javaPath;
    }
}
