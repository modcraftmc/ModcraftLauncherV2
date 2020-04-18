package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.authentification.exception.AuthentificationException;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.modcraftmc.launcher.ui.events.LoginEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.regex.Pattern;

public class LoginController {


    @FXML
    public Pane container;


    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public JFXButton loginbutton;

    @FXML
    public JFXCheckBox keepLogin;

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

    public void tryLogin() {
        changeState(true);
        LoginEvent loginEvent = new LoginEvent();
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        
        try {

            if (emailField.getText().length() == 0 || !emailPattern.matcher(emailField.getText()).find()) {
                throw new AuthentificationException("L'email est invalide", 1);
            }
            if (passwordField.getText().length() == 0) {
                throw new AuthentificationException("Le mot de passe est invalide", 2);
            }

            loginEvent.setSucces(true);

            //Authenticator.auth(emailField.getText(), passwordField.getText());
        } catch (AuthentificationException e) {
            loginEvent.setSucces(false);
            changeState(false);
            ModcraftLauncher.LOGGER.warning(e.getMessage());
        }

        Event.fireEvent(ModcraftApplication.window, loginEvent);
    }

    public void changeState(boolean value) {
        loginbutton.setDisable(value);
        emailField.setDisable(value);
        passwordField.setDisable(value);
    }

}
