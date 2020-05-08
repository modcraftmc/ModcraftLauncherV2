package fr.modcraftmc.launcher.libs.settings;

public class Settings {

    public String version;

    public String accesToken;

    public Boolean discordRPC;

    public Boolean keepLogin;

    public String locale;

    public Settings(String version, String accesToken, Boolean discordRPC, Boolean keepLogin, String locale) {
        this.version = version;
        this.accesToken = accesToken;
        this.discordRPC = discordRPC;
        this.keepLogin = keepLogin;
        this.locale = locale;
    }

}
