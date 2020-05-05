package fr.modcraftmc.modal;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

public class AlertBuilder  {

    private Optional<ButtonType> result;

    private Window window;
    private String title, content;
    private ButtonsType buttonsType;
    private Alert.AlertType alertType;
    private Alert alert;
    private int errorKey;

    public AlertBuilder(Window window, String title, String content,int errorKey , ButtonsType type, Alert.AlertType alertType) {
        this.window = window;
        this.title = title;
        this.content = content;
        this.buttonsType = type;
        this.alertType = alertType;
        this.errorKey = errorKey;

    }

    public AlertBuilder show() {


        alert = new Alert(alertType);

        alert.setTitle(title);
        alert.initOwner(window);
        alert.setHeaderText(content);

        if (buttonsType == buttonsType.YES_OR_NO) {
            ButtonType okButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);

        } else if (buttonsType == buttonsType.OK) {
            ButtonType okButton = new ButtonType("Accepter", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Annuler", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
        } else if(buttonsType == buttonsType.JUST_OK) {
            ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.YES);
            alert.setContentText("Code d'erreur : " + errorKey);
            alert.getButtonTypes().setAll(okButton);
        }
        result = alert.showAndWait();
        return this;
    }

    public Optional<ButtonType> getResult() {
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ButtonsType getAlertType() {
        return buttonsType;
    }

    public enum ButtonsType {
        YES_OR_NO, OK, JUST_OK
    }
}
