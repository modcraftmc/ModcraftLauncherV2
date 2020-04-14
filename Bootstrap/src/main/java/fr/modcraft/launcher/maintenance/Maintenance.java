package fr.modcraft.launcher.maintenance;

public class Maintenance {

    private boolean maintenance, exit;
    private String infos;

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
