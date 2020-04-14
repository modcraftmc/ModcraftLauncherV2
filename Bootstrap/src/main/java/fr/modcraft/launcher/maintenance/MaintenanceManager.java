package fr.modcraft.launcher.maintenance;

import fr.modcraft.launcher.utils.JsonUtils;

import java.io.IOException;

public class MaintenanceManager {

    private boolean maintenance;
    private boolean exit;
    private String infos;

    public  void checkIfMaintenance() {

        try {
            Maintenance maintenance = JsonUtils.readJson("http://v1.modcraftmc.fr/server.json");
            this.maintenance = maintenance.isMaintenance();
            this.exit = maintenance.isExit();
            this.infos = maintenance.getInfos();

        } catch (IOException e) {
            e.printStackTrace();
            maintenance = true;
        }

    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public boolean isExit() {
        return exit;
    }

    public String getInfos() {
        return infos;


    }
}
