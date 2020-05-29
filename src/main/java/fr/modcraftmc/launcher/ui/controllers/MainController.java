package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.core.news.News;
import fr.modcraftmc.launcher.core.news.NewsManager;
import fr.modcraftmc.launcher.core.servers.Server;
import fr.modcraftmc.launcher.core.servers.ServerManager;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.modcraftmc.launcher.ui.events.OptionsEvent;
import fr.modcraftmc.launcher.ui.events.PlayEvent;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;


public class MainController {

    private Server selectedServer = ServerManager.getServerList().get(0);

    @FXML
    public AnchorPane container;

    @FXML
    public ImageView userlogo;

    @FXML
    public Label username;

    @FXML
    public HBox newsbox;

    @FXML
    public Rectangle drag;

    @FXML
    public VBox vboxmenu;

    @FXML
    public Label servername;

    @FXML
    public Label playerlist;

    public List<Button> buttonList = new ArrayList<>();


    public void load() {

        drag.setFill(Color.TRANSPARENT);
        AtomicReference<Double> sx = new AtomicReference<>((double) 0), sy = new AtomicReference<>((double) 0);
        drag.addEventFilter(MOUSE_PRESSED, e -> { sx.set(e.getScreenX() - ModcraftApplication.window.getX()); sy.set(e.getScreenY() - ModcraftApplication.window.getY()); });
        drag.addEventFilter(MOUSE_DRAGGED, e -> { ModcraftApplication.window.setX(e.getScreenX() - sx.get()); ModcraftApplication.window.setY(e.getScreenY() - sy.get()); });

        userlogo.setImage(new Image("https://minotar.net/avatar/" + Authenticator.authInfos.getUsername()));
        username.setText(Authenticator.authInfos.getUsername());

        servername.setText(ServerManager.getServerList().get(0).name);
        playerlist.setText(ModcraftLauncher.serverPingerThread.getResponse().getPlayers().getOnline() + "/" + ModcraftLauncher.serverPingerThread.getResponse().getPlayers().getMax());

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

        for (News news : NewsManager.getNewsList()) {
            Pane pane = new Pane();
            pane.setBackground(new Background(new BackgroundImage(new Image(news.backgroundurl), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            Label title = new Label(news.title);
            Label text = new Label(news.text);
            Label date = new Label(news.date);
            newsbox.getChildren().addAll(title, text, date);
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

    public void options() {
        OptionsEvent event = new OptionsEvent();
        Event.fireEvent(ModcraftApplication.window, event);

    }

    public void switchServer(int server) {
        selectedServer = ServerManager.getServerList().get(server);
        servername.setText(ServerManager.getServerList().get(server).name);
    }

    public void setPlayerlist(String text) {
        ModcraftLauncher.serverPingerThread.run();
        Platform.runLater(()-> playerlist.setText(text));
    }

    public void toGoSettings() {
        Scene switchto = ModcraftApplication.optionScene;
        switchto.getStylesheets().add(ModcraftApplication.resourcesManager.getResource("global.css").toExternalForm());
        switchto.setFill(Color.TRANSPARENT);
        ModcraftApplication.window.setScene(switchto);
    }

}
