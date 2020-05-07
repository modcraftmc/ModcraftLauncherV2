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

import java.util.ArrayList;
import java.util.List;


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
    public Label servername;

    @FXML
    public Label playerlist;

    public List<Button> buttonList = new ArrayList<>();


    public void load() {

        userlogo.setImage(new Image("https://minotar.net/avatar/" + Authenticator.authInfos.getUsername()));
        username.setText(Authenticator.authInfos.getUsername());

        for (Server server : ServerManager.getServerList()) {
            Button button = new Button(server.name);
            button.getStyleClass().addAll("play", "serverbtn");
            button.setPrefWidth(287);
            button.setPrefHeight(58);
            button.setId(server.id);
            if (server.maintenance) button.setDisable(true);
            buttonList.add(button);

            Label spacer = new Label("");
            spacer.setPrefWidth(287);
            spacer.setPrefHeight(27);
            vboxmenu.getChildren().addAll(button, spacer);
        }


        for (Button button : buttonList) {
            button.setText(ServerManager.getServerList().get(Integer.parseInt(button.getId()) - 1).name);
            button.setOnMouseClicked(event -> switchServer(Integer.parseInt(button.getId()) - 1));
        }

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

    public void switchServer(int server) {
        selectedServer = ServerManager.getServerList().get(server - 1);
        servername.setText(ServerManager.getServerList().get(server - 1).name);
    }

    public void setPlayerlist(String text) {
        ModcraftLauncher.serverPingerThread.run();
        playerlist.setText(text);
    }
}
