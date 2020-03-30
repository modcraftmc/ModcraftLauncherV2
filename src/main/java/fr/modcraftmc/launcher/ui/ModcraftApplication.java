package fr.modcraftmc.launcher.ui;

import fr.modcraftmc.launcher.core.Constants;
import fr.modcraftmc.launcher.core.resources.ResourcesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class ModcraftApplication extends Application {

    public static ResourcesManager resourcesManager = new ResourcesManager();


    @Override
    public void start(Stage stage) throws Exception {

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(Constants.TITLE);
        stage.setResizable(false);

        FXMLLoader loader = new FXMLLoader((URL) resourcesManager.getResource("login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.getIcons().add(new Image(resourcesManager.getResource("favicon.png").toString()));
        stage.show();


    }
}
