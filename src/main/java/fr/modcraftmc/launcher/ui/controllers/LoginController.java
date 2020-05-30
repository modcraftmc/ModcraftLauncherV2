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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

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

        drag.setFill(Color.TRANSPARENT);
        AtomicReference<Double> sx = new AtomicReference<>((double) 0), sy = new AtomicReference<>((double) 0);
        drag.addEventFilter(MOUSE_PRESSED, e -> { sx.set(e.getScreenX() - ModcraftApplication.window.getX()); sy.set(e.getScreenY() - ModcraftApplication.window.getY()); });
        drag.addEventFilter(MOUSE_DRAGGED, e -> { ModcraftApplication.window.setX(e.getScreenX() - sx.get()); ModcraftApplication.window.setY(e.getScreenY() - sy.get()); });

    }

    public void tryLogin() {
        changeState(true);
        LoginEvent loginEvent = new LoginEvent();
        Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        
        try {

            if (emailField.getText().length() == 0 || !emailPattern.matcher(emailField.getText()).find()) {
                //throw new AuthentificationException("L'email est invalide", 1);
            }
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

        boolean logged = false;

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
