package fr.modcraftmc.launcher.maintenance;

import fr.modcraftmc.launcher.utils.JsonUtils;

import java.io.IOException;

public class MaintenanceManager {

    private boolean Ismaintenance;
    Maintenance maintenance;


    public MaintenanceManager() {

        try {
             maintenance = JsonUtils.readJson(Maintenance.class,"http://v1.modcraftmc.fr/server.json");
            this.Ismaintenance = maintenance.isMaintenance();

        } catch (IOException e) {
            e.printStackTrace();
            Ismaintenance = true;
        }

    }

    public boolean isMaintenance() {
        return Ismaintenance;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }
}
