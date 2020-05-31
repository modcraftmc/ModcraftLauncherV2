package fr.modcraftmc.launcher.core.settings;

public class Settings {

    public String version;

    public String accesToken;

    public Boolean discordRPC;

    public Boolean keepLogin;

    public Boolean keepLauncherOpen;

    public String locale;

    public int ram;

    public boolean autoram;

    public boolean customFolder;

    public String customFolderPath;


    public Settings(String version, String accesToken, Boolean discordRPC, Boolean keepLogin, Boolean keepLauncherOpen,String locale, int ram, boolean autoram,
                    boolean customFolder, String customFolderPath) {
        this.version = version;
        this.accesToken = accesToken;
        this.discordRPC = discordRPC;
        this.keepLogin = keepLogin;
        this.keepLauncherOpen = keepLauncherOpen;
        this.locale = locale;
        this.ram = ram;
        this.autoram = autoram;
        this.customFolder = customFolder;
        this.customFolderPath = customFolderPath;
    }

}
