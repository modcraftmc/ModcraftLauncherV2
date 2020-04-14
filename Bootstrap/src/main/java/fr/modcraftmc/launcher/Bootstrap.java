package fr.modcraftmc.launcher;

import fr.modcraftmc.launcher.alert.AlertBuilder;
import fr.modcraftmc.launcher.downloader.DownloaderManager;
import fr.modcraftmc.launcher.downloader.gameUpdater.EnumModcraft;
import fr.modcraftmc.launcher.downloader.gameUpdater.GameUpdater;
import fr.modcraftmc.launcher.maintenance.MaintenanceManager;
import fr.modcraftmc.launcher.utils.JavaUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Bootstrap extends Application {

    public static final File DIR = new File(System.getProperty("app.data") + "/.modcraftmc");

    private static ProgressBar progressBar = new ProgressBar();
    public static Stage stage;

    @Override
    public void start(Stage stage) {
        System.out.println("JAVA INFORMATIONS : " + JavaUtils.getArchitecture() + " | " + JavaUtils.getVersion());
        Bootstrap.stage = stage;
        stage.initStyle(StageStyle.DECORATED);
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        stage.setTitle("ModcraftMC");
        stage.getIcons().add(new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("favicon.png")));
        stage.setScene(scene);
        stage.setHeight(60);
        stage.setWidth(300);

        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        stage.setX((screen.getWidth() - stage.getWidth()) / 2);
        stage.setY((screen.getHeight() - stage.getHeight()) / 2);
        initComponents(root);


        MaintenanceManager maintenanceManager = new MaintenanceManager();
        maintenanceManager.checkIfMaintenance();

        if (maintenanceManager.isMaintenance()) {
            AlertBuilder alertBuilder = new AlertBuilder("Maintenance", maintenanceManager.getInfos(), AlertBuilder.ButtonsType.JUST_OK, Alert.AlertType.ERROR);
            alertBuilder.show();
            if (maintenanceManager.isExit()) {
                System.exit(0);
            }
        }

        if (!JavaUtils.is64Bits()) {
            DownloaderManager.askToDownload();
        } else {
            new Thread(Bootstrap::run).start();
        }
    }

    private void initComponents(StackPane root) {
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(30);
        progressBar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        root.getStylesheets().add(ClassLoader.getSystemClassLoader().getResource("bar.css").toExternalForm());
        root.getChildren().addAll(progressBar);
    }

    public static void run() {

        try {
            doUpdate();
        } catch (Exception e) {
        }

        try {
            Thread.sleep(2000);
            launchLauncher();
        } catch (InterruptedException e) {
        }
    }

    public static void doUpdate() throws Exception {

        GameUpdater.setToDownload(EnumModcraft.BOOTSTRAP);
        GameUpdater gameUpdater = new GameUpdater("http://v1.modcraftmc.fr:100/gameupdater/", DIR);
        gameUpdater.start();



    }

    private static void launchLauncher() {

    }

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setTitle(String text) {
        Platform.runLater(() -> stage.setTitle(text));
    }
}
