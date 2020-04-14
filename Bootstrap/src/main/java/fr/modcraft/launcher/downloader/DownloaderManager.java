package fr.modcraft.launcher.downloader;

import fr.modcraft.launcher.Bootstrap;
import fr.modcraft.launcher.alert.AlertBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

import java.io.IOException;

public class DownloaderManager {


    public static void download() {
        ProgressBar bar = Bootstrap.getProgressBar();
        Bootstrap.setTitle("Téléchargement");
        bar.progressProperty().unbind();
        Task<Void> task = new DownloadTask("http://v1.modcraftmc.fr/java/java.exe");
        bar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    public static void askToDownload() {
        AlertBuilder alertBuilder = new AlertBuilder("ModcraftMC - java","Java 64 bits n'est pas detecté, voulez vous l'installer ?", AlertBuilder.ButtonsType.OK, Alert.AlertType.CONFIRMATION);
        alertBuilder.show();
        if (!alertBuilder.getResult().get().getButtonData().isCancelButton()) {
            System.out.println("downloading java");
            new Thread(DownloaderManager::download).start();
        } else {
            new Thread(Bootstrap::run).start();
        }

    }

    public static void startJavaExe() {
        AlertBuilder alertBuilder = new AlertBuilder("ModcraftMC - java","L'instalation de java va commencer, merci de suivre les éptapes indiquées.", AlertBuilder.ButtonsType.OK, Alert.AlertType.CONFIRMATION);
        alertBuilder.show();
        if (!alertBuilder.getResult().get().getButtonData().isCancelButton()) {

            String exe = DownloadTask.getTempPath().toAbsolutePath().toString();
            Runtime runtime = Runtime.getRuntime();
            try {
                System.out.println("staring java executable at " + exe);
                Process p = runtime.exec("cmd /c " + exe);
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                Platform.runLater(() -> Bootstrap.stage.show());
                Bootstrap.setTitle("ModcraftMC");
                new Thread(Bootstrap::run).start();
            }

        }
    }
}



