package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class LoginController   {

    @FXML
    public Pane container;



    public void close() {

        AnimationFX close = new FadeOut(container);
        close.setSpeed(5D);
        close.setResetOnFinished(true);
        close.play();
        close.setOnFinished(event -> System.exit(0));

    }


    public void hide() {

        AnimationFX hide = new FadeOut(container);
        hide.setSpeed(5D);
        hide.setResetOnFinished(true);
        hide.play();
        hide.setOnFinished(event -> ModcraftApplication.window.setIconified(true));

    }
}
