package fr.modcraftmc.launcher.libs.settings;

public class Settings {

    public String accesToken;
    public String version;
    public Boolean keepLogin;
    public Boolean discordRPC;

    public Settings() {

    }

    public Settings(String version, String accesToken, Boolean discordRPC, Boolean keepLogin) {
        this.version = version;
        this.accesToken = accesToken;
        this.discordRPC = discordRPC;
        this.keepLogin = keepLogin;

    }

}
