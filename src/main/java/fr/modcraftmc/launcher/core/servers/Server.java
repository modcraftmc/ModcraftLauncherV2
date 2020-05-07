package fr.modcraftmc.launcher.core.servers;

public class Server {

    public String name;

    public String update_url;

    public boolean maintenance;

    public Server(String name, String update_url, boolean maintenance) {
        this.name = name;
        this.update_url = update_url;
        this.maintenance = maintenance;
    }
}
