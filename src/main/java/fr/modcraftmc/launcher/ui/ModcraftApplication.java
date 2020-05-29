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
    public static Stage window;
    private FXMLLoader loader;
    private static Parent login;
    public static Parent options;
    public static Scene mainScene;
    public static Scene optionScene;

    public static Parent main;
    public static MainController mainController;
    public static OptionsController optionsController;
    public static boolean mainLoaded = false;

    private static Parent download;

    @Override
    public void start(Stage stage) throws Exception {

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
        optionScene = new Scene(options);

        Scene scene = new Scene(logincontroller.checkToken() ? main : login);

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
                mainController.load();
                mainLoaded = true;
                switchScene(main);
            }

        });

        window.addEventHandler(PlayEvent.PLAY, event -> {
                switchScene(download);
                new Thread(() -> downloadController.download(event.getServer())).start();

        });

        scene.setOnKeyPressed(event -> {
            if (logincontroller.emailField.isFocused() || logincontroller.passwordField.isFocused()) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    logincontroller.tryLogin();
                }
            }
        });

        window.setOnCloseRequest(event -> ModcraftLauncher.settingsManager.save());
    }

    public void switchScene(Parent scene) {
        mainScene = new Scene(scene);
        mainScene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        mainScene.setFill(Color.TRANSPARENT);
        window.setScene(mainScene);
    }


}
