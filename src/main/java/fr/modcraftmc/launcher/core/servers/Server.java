package fr.modcraftmc.launcher.core.servers;

public class Server {

    public String id;

    public String name;

    public String update_url;

    public boolean maintenance;

    public Server(String id, String name, String update_url, boolean maintenance) {
        this.id = id;
        this.name = name;
        this.update_url = update_url;
        this.maintenance = maintenance;
    }
}
