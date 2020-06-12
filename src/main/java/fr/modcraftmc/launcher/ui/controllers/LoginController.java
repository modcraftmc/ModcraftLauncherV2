package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.modcraftmc.launcher.libs.authentification.exception.AuthentificationException;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.modcraftmc.launcher.ui.events.LoginEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class LoginController {


    @FXML
    public AnchorPane container;

    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public JFXButton loginbutton;

    @FXML
    public JFXCheckBox keepLogin;

    @FXML
    public Rectangle drag;

    @FXML
    public Hyperlink passwordLost;


    public void close() {

        AnimationFX close = new FadeOut(container);
        close.setSpeed(5D);
        close.setResetOnFinished(true);
        close.play();
        close.setOnFinished(event -> System.exit(0));
    }

    public void setup() {

        emailField.clear();
        passwordField.clear();
        changeState(false);

    }

    public void tryLogin() {
        changeState(true);
        LoginEvent loginEvent = new LoginEvent();
        try {

            if (passwordField.getText().length() == 0) {
                throw new AuthentificationException("Le mot de passe est invalide", 2);
            }

            loginEvent.setSucces(true);

            Authenticator.auth(emailField.getText(), passwordField.getText());
        } catch (AuthentificationException e) {
            ModcraftLauncher.LOGGER.warning(e.getMessage());
            loginEvent.setSucces(false);
            changeState(false);
        }

        ModcraftLauncher.settingsManager.getSettings().keepLogin = keepLogin.isSelected();
        if (keepLogin.isSelected() && loginEvent.getSucces()) {
            ModcraftLauncher.settingsManager.getSettings().accesToken = emailField.getText() + ";" + passwordField.getText();
        }

        ModcraftLauncher.settingsManager.save();
        Event.fireEvent(ModcraftApplication.window, loginEvent);
    }

    public boolean checkToken() {

        if (!ModcraftLauncher.settingsManager.getSettings().keepLogin) return false;

        boolean logged;

        String[] tokens = ModcraftLauncher.settingsManager.getSettings().accesToken.split(";");
        if (tokens.length != 2) {
            return false;
        }

        if (tokens[0] == null || tokens[1] == null) return false;

        emailField.setText(tokens[0]);
        passwordField.setText(tokens[1]);
        LoginEvent loginEvent = new LoginEvent();

        try {

            if (passwordField.getText().length() == 0) {
                throw new AuthentificationException("Le mot de passe est invalide", 2);
            }

            loginEvent.setSucces(true);
            logged = true;

            Authenticator.auth(emailField.getText(), passwordField.getText());
        } catch (AuthentificationException e) {
            logged = false;
            ModcraftLauncher.LOGGER.warning(e.getMessage());
            loginEvent.setSucces(false);
            changeState(false);
        }
        Event.fireEvent(ModcraftApplication.window, loginEvent);
        return logged;

    }

    public void changeState(boolean value) {
        loginbutton.setDisable(value);
        emailField.setDisable(value);
        passwordField.setDisable(value);
    }


}
