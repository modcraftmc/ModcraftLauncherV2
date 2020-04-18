package fr.modcraftmc.launcher.libs.settings;

public class Settings {

    private String accesToken;
    private Boolean discordRPC;

    public Settings() {

    }

    public Settings(String accesToken, Boolean discordRPC) {
        this.accesToken = accesToken;
        this.discordRPC = discordRPC;

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
}
