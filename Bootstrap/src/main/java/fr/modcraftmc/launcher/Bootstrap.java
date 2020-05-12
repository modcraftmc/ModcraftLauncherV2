package fr.modcraftmc.launcher;

import fr.modcraftmc.FilesManager;
import fr.modcraftmc.launcher.maintenance.MaintenanceManager;
import fr.modcraftmc.launcher.utils.JavaUtils;
import fr.modcraftmc.modal.AlertBuilder;
import javafx.application.Application;
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
import ma.forix.gameUpdater.EnumModcraft;
import ma.forix.gameUpdater.GameUpdater;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Bootstrap extends Application {

    public final static Logger LOGGER = LoggerFactory.getLogger("ModcraftMC-Bootstrap");

    public static MaintenanceManager maintenanceManager = new MaintenanceManager();


    private static final ProgressBar progressBar = new ProgressBar();
    public static Stage stage;


    @Override
    public void start(Stage stage) {
        Bootstrap.LOGGER.info("Starting system check...");


        if (maintenanceManager.isMaintenance()) {
            AlertBuilder TEST_ALERT = new AlertBuilder(stage, "ModcraftMC", maintenanceManager.getMaintenance().getInfos(), 10, AlertBuilder.ButtonsType.JUST_OK, Alert.AlertType.ERROR);
            TEST_ALERT.show();
            if (maintenanceManager.getMaintenance().isExit()) {
                System.exit(0);
            }
        }

        LOGGER.info("Java architecture : " + JavaUtils.getArchitecture());
        LOGGER.info("Java version : " + JavaUtils.getVersion());
        Bootstrap.stage = stage;
        stage.initStyle(StageStyle.DECORATED);
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        stage.setTitle("ModcraftMC");
        stage.getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream("favicon.png"))));
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

        new Thread(Bootstrap::run).start();
    }

    private void initComponents(StackPane root) {
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(30);
        progressBar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        root.getStylesheets().add(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("bar.css")).toExternalForm());
        root.getChildren().addAll(progressBar);
    }

    public static void run() {

         doUpdate();

    }

    public static void doUpdate() {

        GameUpdater.setToDownload(EnumModcraft.LAUNCHER);
        GameUpdater gameUpdater = new GameUpdater("http://v1.modcraftmc.fr:100/beta/", FilesManager.LAUNCHER_PATH, progressBar);
        gameUpdater.setDeleter(false);
        gameUpdater.updater().setOnSucceeded((event -> new Thread(Bootstrap::launchLauncher).start()));
        gameUpdater.start();

    }

    private static void launchLauncher() {
        System.out.println("aaa");
            File dir = new File(FilesManager.LAUNCHER_PATH, "launcher.jar").getParentFile();

            ProcessBuilder builder = new ProcessBuilder();
            builder.command("java", "-jar", "launcher.jar");
            builder.directory(dir);
            try {
                builder.start();
                Thread.sleep(2000);
                System.exit(0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
    }

}
