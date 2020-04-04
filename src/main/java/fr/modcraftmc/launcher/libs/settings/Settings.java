package fr.modcraftmc.launcher.libs.settings;

import fr.modcraftmc.launcher.core.ModcraftLauncher;

import java.io.File;

public class Settings {

    public static void setModcraftPath(File path){
        ModcraftLauncher.DEFAULT_PATH = path;
        ModcraftLauncher.refreshPaths();
    }
}
