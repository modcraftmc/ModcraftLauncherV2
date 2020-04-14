package fr.modcraft.launcher;

import fr.modcraft.launcher.alert.AlertBuilder;
import fr.modcraft.launcher.downloader.DownloaderManager;
import fr.modcraft.launcher.maintenance.MaintenanceManager;
import fr.modcraft.launcher.utils.JavaUtils;
import fr.theshark34.openlauncherlib.JavaUtil;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;
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

    public static final File DIR = new File(GameDirGenerator.createGameDir("modcraft/"), "Launcher/beta");
    private static CrashReporter crashReporter = new CrashReporter("Bootstrap-Crash", new File(DIR, "crash/"));

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
            crashReporter.catchError(e, "Impossible de mettre à jour le Launcher !");
        }

        try {
            Thread.sleep(2000);
            launchLauncher();
        } catch (LaunchException | InterruptedException e) {
            crashReporter.catchError(e, "Impossible de lancer le Launcher !");
        }
    }

    public static void doUpdate() throws Exception {

        SUpdate su = new SUpdate("http://v1.modcraftmc.fr:300", DIR);
        su.getServerRequester().setRewriteEnabled(true);
        su.start();
    }

    private static void launchLauncher() throws LaunchException {
        JavaUtil.setJavapath("");
        ClasspathConstructor constructor = new ClasspathConstructor();

        ExploredDirectory gameDir = Explorer.dir(DIR);

        constructor.add(gameDir.get("launcher.jar"));
        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.modcraft.launcher.ModcraftLauncher", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);


        Process p = launcher.launch();
        try {
            Platform.runLater(() -> Bootstrap.stage.hide());
            p.waitFor();
        } catch (InterruptedException ignored) {
        }
        System.exit(0);
    }

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setTitle(String text) {
        Platform.runLater(() -> stage.setTitle(text));
    }
}
