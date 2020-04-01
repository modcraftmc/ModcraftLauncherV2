package fr.modcraftmc.launcher.core;

import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;

public class ModcraftLauncher {

    public final static String OS_NAME = System.getProperty("os.name");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static File DEFAULT_PATH = new File(System.getenv("appdata") + "\\.modcraftmc\\");
    public final static File OPTIONS_PATH = new File(DEFAULT_PATH, "modcraftlauncher.json");
    public static final File INSTANCES_PATH = new File(DEFAULT_PATH, "instances");

    public static void main(String[] args) throws IOException {

        if (!DEFAULT_PATH.exists()) {
            DEFAULT_PATH.mkdirs();
        }
        if (!OPTIONS_PATH.exists()) {
            OPTIONS_PATH.createNewFile();
        }
        if (!INSTANCES_PATH.exists()) {
            INSTANCES_PATH.mkdirs();
        }


        //TODO: get information form launcher.modcraftmc.fr
        getInformations();

        //TODO: check system
        checkEnvironement();

        Application.launch(ModcraftApplication.class, args);

    }

    public static void getInformations() {

    }

    public static void checkEnvironement() {

    }

}
