package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.servers.Server;
import fr.modcraftmc.launcher.core.servers.ServerManager;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.modcraftmc.launcher.ui.events.PlayEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class MainController {

    private Server selectedServer = ServerManager.getServerList().get(0);

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

    @FXML
    public Button server1;

    @FXML
    public Button server2;

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
        if (selectedServer == null) {
            return;
        }

        ModcraftLauncher.LOGGER.info("Server to connect : " + selectedServer.name);
        PlayEvent event = new PlayEvent(selectedServer);
        Event.fireEvent(ModcraftApplication.window, event);

    }

    public void server1()  {
        selectedServer = ServerManager.getServerList().get(0);
        server2.getStyleClass().add("disabled");
        server1.getStyleClass().remove("disabled");

    }
    public void server2() {
        selectedServer = ServerManager.getServerList().get(1);
        server1.getStyleClass().add("disabled");
        server2.getStyleClass().remove("disabled");

    }
}
