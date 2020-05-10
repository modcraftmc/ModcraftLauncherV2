package fr.modcraftmc.launcher.maintenance;

public class Maintenance {

    private final boolean maintenance;
    private final boolean exit;
    private final String infos;

    public Maintenance(boolean maintenance, boolean exit,String infos) {
        this.maintenance = maintenance;
        this.exit = exit;
        this.infos = infos;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public String getInfos() {
        return infos;
    }

    public boolean isExit() {
        return exit;
    }
}
