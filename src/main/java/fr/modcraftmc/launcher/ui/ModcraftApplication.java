package fr.modcraftmc.launcher.ui;

import fr.modcraftmc.launcher.core.Constants;
import fr.modcraftmc.launcher.core.resources.ResourcesManager;
import fr.modcraftmc.launcher.ui.controllers.DownloadController;
import fr.modcraftmc.launcher.ui.controllers.LoginController;
import fr.modcraftmc.launcher.ui.controllers.MainController;
import fr.modcraftmc.launcher.ui.events.LoginEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class ModcraftApplication extends Application {

    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static Stage window;
    private FXMLLoader loader;
    private static Parent login;
    private static Parent main;
    private static Parent download;


    @Override
    public void start(Stage stage) throws Exception {
        window = stage;


        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(Constants.TITLE);
        stage.setResizable(false);

        loader = new FXMLLoader(resourcesManager.getResource("login.fxml"));
        login = loader.load();
        LoginController logincontroller = loader.getController();

        TextField emailField = logincontroller.emailField;
        PasswordField passwordField = logincontroller.passwordField;
        Button loginButton = logincontroller.loginbutton;

        loader = new FXMLLoader(resourcesManager.getResource("main.fxml"));
        main = loader.load();
        MainController maincontroller = loader.getController();

        loader = new FXMLLoader(resourcesManager.getResource("download.fxml"));
        download = loader.load();
        DownloadController downloadController = loader.getController();

        MediaPlayer player = new MediaPlayer(new Media(resourcesManager.getResource("vi.mp4").toURI().toURL().toString()));
        player.setAutoPlay(true);



        Scene scene = new Scene(download);
        scene.getStylesheets().add(resourcesManager.getResource("login.css").toExternalForm());
        scene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        downloadController.media.setMediaPlayer(player);

        scene.setOnKeyPressed(event -> {
            if (emailField.isFocused() || passwordField.isFocused()) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                        logincontroller.tryLogin();
                }
            }

        });


        stage.setScene(scene);
        stage.getIcons().add(new Image(resourcesManager.getResource("favicon.png").toString()));
        stage.show();

        AtomicReference<Double> sx = new AtomicReference<>((double) 0), sy = new AtomicReference<>((double) 0);
        stage.addEventFilter(MOUSE_PRESSED, e -> { sx.set(e.getScreenX() - window.getX()); sy.set(e.getScreenY() - window.getY()); });
        stage.addEventFilter(MOUSE_DRAGGED, e -> { window.setX(e.getScreenX() - sx.get()); window.setY(e.getScreenY() - sy.get()); });


        logincontroller.passwordLost.setOnAction(event -> {
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.modcraftmc.fr/passwordlost"));
                } catch (IOException | URISyntaxException ignored) {
                }
            }
        });

        window.addEventHandler(LoginEvent.LOGIN, event -> {
            if (event.getSucces()) switchScene(main);
        });

    }

    public void switchScene(Parent scene) {
        Scene switchto = new Scene(scene);
        window.setScene(switchto);

    }
}
