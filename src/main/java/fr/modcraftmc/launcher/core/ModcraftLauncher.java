package fr.modcraftmc.launcher.core;

import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.application.Application;

public class ModcraftLauncher {

    public final static String OS_NAME = System.getProperty("os.name");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static String DEFAULT_PATH = System.getenv("appdata") + "\\.modcraftmc";

    public static void main(String[] args) {

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
