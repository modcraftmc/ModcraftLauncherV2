package fr.modcraftmc.launcher.core;

import fr.modcraftmc.launcher.libs.files.FilesManager;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.application.Application;

import java.io.IOException;

public class ModcraftLauncher {

    public final static String OS_NAME = System.getProperty("os.name");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static FilesManager filesManager = new FilesManager();


    public static void main(String[] args) throws IOException {

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
