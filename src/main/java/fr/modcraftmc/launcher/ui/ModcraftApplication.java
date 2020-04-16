package fr.modcraftmc.launcher.ui;

import fr.modcraftmc.launcher.core.Constants;
import fr.modcraftmc.launcher.core.resources.ResourcesManager;
import fr.modcraftmc.launcher.ui.controllers.LoginController;
import fr.modcraftmc.launcher.ui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class ModcraftApplication extends Application {

    public static ResourcesManager resourcesManager = new ResourcesManager();
    public static Stage window;
    private double sx = 0, sy = 0;
    private FXMLLoader loader;
    private static Parent login;
    private static Parent main;


    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(Constants.TITLE);
        stage.setResizable(false);

        loader = new FXMLLoader(resourcesManager.getResource("login.fxml"));
        login = loader.load();
        LoginController logincontroller = loader.getController();

        loader = new FXMLLoader(resourcesManager.getResource("main.fxml"));
        main = loader.load();
        MainController Maincontroller = loader.getController();



        Scene scene = new Scene(login);
        scene.getStylesheets().add(resourcesManager.getResource("login.css").toExternalForm());
        scene.getStylesheets().add(resourcesManager.getResource("global.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.getIcons().add(new Image(resourcesManager.getResource("favicon.png").toString()));
        stage.show();

        stage.addEventFilter(MOUSE_PRESSED, e -> { sx = e.getScreenX() - window.getX(); sy = e.getScreenY() - window.getY(); });
        stage.addEventFilter(MOUSE_DRAGGED, e -> { window.setX(e.getScreenX() - sx); window.setY(e.getScreenY() - sy); });



        logincontroller.passwordLost.setOnAction(e -> {
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.modcraftmc.fr/passwordlost"));
                } catch (IOException | URISyntaxException e1) {
                }
            }
        });

        //CrashReporter.catchException(new Exception(), stage);

    }

    public static void loginFinish() {
        Scene scene = new Scene(main);
        window.setScene(scene);

    }
}
