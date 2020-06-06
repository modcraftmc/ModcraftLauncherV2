package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.servers.Server;
import fr.modcraftmc.launcher.core.servers.ServerManager;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.modcraftmc.launcher.libs.serverpinger.ServerPingerThread;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.modcraftmc.launcher.ui.events.PlayEvent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class MainController {

    public static ServerPingerThread serverPingerThread = new ServerPingerThread();

    @FXML
    public AnchorPane container;

    @FXML
    public Label servername;
    private final Server selectedServer = ServerManager.getServerList().get(0);


    @FXML
    public ImageView userlogo;

    @FXML
    public Label username;
    @FXML
    public Label stats_players;
    @FXML
    public Label stats_ping;

    public void load() {


        Image userimage = new Image("https://minotar.net/avatar/" + Authenticator.authInfos.getUsername());

        if (userimage.getWidth() == 0.0) {
            userimage = new Image("https://minotar.net/avatar/steve");
        }

        userlogo.setImage(userimage);
        username.setText(Authenticator.authInfos.getUsername());

        new Thread(serverPingerThread).start();

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


    public void setPlayerlist(String text) {
        Platform.runLater(() -> stats_players.setText(text));
    }

    public void toGoSettings() {
        Scene switchto = ModcraftApplication.optionScene;
        switchto.getStylesheets().add(ModcraftApplication.resourcesManager.getResource("global.css").toExternalForm());
        switchto.setFill(Color.TRANSPARENT);
        ModcraftApplication.window.setScene(switchto);
    }

}
