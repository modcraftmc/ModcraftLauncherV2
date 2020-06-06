package fr.modcraftmc.launcher.ui;

import fr.modcraftmc.launcher.core.Constants;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.resources.ResourcesManager;
import fr.modcraftmc.launcher.ui.controllers.DownloadController;
import fr.modcraftmc.launcher.ui.controllers.LoginController;
import fr.modcraftmc.launcher.ui.controllers.MainController;
import fr.modcraftmc.launcher.ui.controllers.OptionsController;
import fr.modcraftmc.launcher.ui.events.LoginEvent;
import fr.modcraftmc.launcher.ui.events.PlayEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ma.forix.gameUpdater.GameUpdater;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ModcraftApplication extends Application {

    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static String statusDiscord = "Sur le launcher";
    public static int players = 0;
    public static int maxplayers = 0;

    public static Stage window;
    private FXMLLoader loader;

    public static Parent main;
    private static Parent login;
    private static Parent download;
    public static Parent options;

    public static Scene mainScene;
    public static Scene optionScene;
    public static Scene loginScene;
    public static Scene downloadScene;

    public static ModcraftApplication instance;

    public static MainController mainController;
    public static OptionsController optionsController;
    public static boolean mainLoaded = false;


    @Override
    public void start(Stage stage) throws Exception {
        instance = this;

        window = stage;

        GameUpdater.checkServer();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(Constants.TITLE);
        stage.setResizable(false);

        loader = new FXMLLoader(resourcesManager.getResource("login.fxml"));
        login = loader.load();
        LoginController logincontroller = loader.getController();
        logincontroller.setup();

        logincontroller.keepLogin.setSelected(ModcraftLauncher.settingsManager.getSettings().keepLogin);

        loader = new FXMLLoader(resourcesManager.getResource("main.fxml"));
        main = loader.load();
        mainController = loader.getController();

        loader = new FXMLLoader(resourcesManager.getResource("download.fxml"));
        download = loader.load();
        DownloadController downloadController = loader.getController();

        loader = new FXMLLoader(resourcesManager.getResource("options.fxml"));
        options = loader.load();
        optionsController = loader.getController();
        optionsController.setup();

        mainScene = new Scene(main);
        loginScene = new Scene(login);
        optionScene = new Scene(options);
        downloadScene = new Scene(download);



        Scene scene;
        if (logincontroller.checkToken()) {
            scene = mainScene;
            mainController.load();

        } else {
            scene = loginScene;
        }


        scene.getStylesheets().add(resourcesManager.getResource("login.css").toExternalForm());
        scene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.getIcons().add(new Image(resourcesManager.getResource("favicon.png").toString()));
        stage.show();


        logincontroller.passwordLost.setOnAction(event -> {
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI("https://cestpasencorefini.sorry"));
                } catch (IOException | URISyntaxException ignored) {}
            }
        });



        window.addEventHandler(LoginEvent.LOGIN, event -> {
            if (event.getSucces())  {
                switchToMain();
            }

        });


        window.addEventHandler(PlayEvent.PLAY, event -> {
                switchToDownload();
                new Thread(() -> downloadController.download(event.getServer())).start();

        });

        scene.setOnKeyPressed(event -> {
            if (logincontroller.emailField.isFocused() || logincontroller.passwordField.isFocused()) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    logincontroller.tryLogin();
                }
            }
        });

        window.setOnCloseRequest(event -> {
            ModcraftLauncher.settingsManager.save();
            System.exit(0);
        });
    }

    public void switchToMain() {
        mainController.load();
        mainLoaded = true;
        mainScene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        mainScene.setFill(Color.TRANSPARENT);
        window.setScene(mainScene);
    }

    public void switchToDownload() {
        downloadScene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        downloadScene.setFill(Color.TRANSPARENT);
        window.setScene(downloadScene);
    }

    public void switchToOptions() {
        optionScene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        optionScene.setFill(Color.TRANSPARENT);
        window.setScene(optionScene);
    }

    public void switchToLogin() {
        loginScene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        loginScene.setFill(Color.TRANSPARENT);
        window.setScene(loginScene);
    }

    public static ModcraftApplication getInstance() {
        return instance;
    }

}
