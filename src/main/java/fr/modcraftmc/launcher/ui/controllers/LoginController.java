package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.authentification.exception.AuthentificationException;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.regex.Pattern;

public class LoginController   {

    @FXML
    public Pane container;


    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;


    @FXML
    public Hyperlink passwordLost;

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

    public void button() {

        try {
            login();
        } catch (AuthentificationException e) {
            ModcraftLauncher.LOGGER.warning(e.getMessage());
        }

    }

    public void login() throws AuthentificationException {
        Pattern emailpattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");


        if (emailField.getText().length() == 0 || !emailpattern.matcher(emailField.getText()).find()) {
            throw new AuthentificationException("L'email est invalide", 1);
        }
        if (passwordField.getText().length() == 0) {
            throw new AuthentificationException("Le mot de passe est invalide", 2);
        }

       // Authenticator.auth(emailField.getText(), passwordField.getText());

        ModcraftApplication.loginFinish();

    }

}
