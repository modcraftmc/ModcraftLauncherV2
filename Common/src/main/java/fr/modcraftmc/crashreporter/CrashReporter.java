package fr.modcraftmc.crashreporter;

import fr.modcraftmc.modal.AlertBuilder;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public class CrashReporter {

    public static void catchException(Throwable e, Window mainWindow, int errorkey) {
        new AlertBuilder(mainWindow, "ModcraftMC", e.getMessage(), errorkey,AlertBuilder.ButtonsType.JUST_OK, Alert.AlertType.ERROR).show();

    }
}
