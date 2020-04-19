package fr.modcraftmc.launcher.libs.settings;

public class Settings {

    private String accesToken;
    private Boolean keepLogin;
    private Boolean discordRPC;

    public Settings() {

    }

    public Settings(String accesToken, Boolean discordRPC, Boolean keepLogin) {
        this.accesToken = accesToken;
        this.discordRPC = discordRPC;
        this.keepLogin = keepLogin;

    }

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }

    public Boolean useDiscordRPC() {
        return discordRPC;
    }

    public void setDiscordRPC(Boolean discordRPC) {
        this.discordRPC = discordRPC;
    }

    public Boolean getKeepLogin() {
        return keepLogin;
    }

    public void setKeepLogin(Boolean keepLogin) {
        this.keepLogin = keepLogin;
    }
}
