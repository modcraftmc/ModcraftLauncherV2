package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class MainController {

    private int selectedServer = 1;

    @FXML
    public Pane container;

    @FXML
    public ImageView userlogo;

    @FXML
    public Label username;

    @FXML
    public HBox newsbox;

    @FXML
    public VBox vboxmenu;

    public void load() {

        userlogo.setImage(new Image("https://minotar.net/avatar/" + Authenticator.authInfos.getUsername()));
        username.setText(Authenticator.authInfos.getUsername());




    }

    public void close() {

        AnimationFX close = new FadeOut(container);
        close.setSpeed(5D);
        close.setResetOnFinished(true);
        close.play();
        close.setOnFinished(event -> System.exit(0));
    }

    public void play() {

    }

    public void server1()  {
        selectedServer = 1;

    }
    public void server2() {
        selectedServer = 2;

    }
}
