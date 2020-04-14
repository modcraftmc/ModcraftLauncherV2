package fr.modcraft.launcher.alert;

import fr.modcraft.launcher.Bootstrap;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertBuilder {

    private Optional<ButtonType> result;

    private String title, content;
    private ButtonsType buttonsType;
    private Alert.AlertType alertType;
    private Alert alert;

    public AlertBuilder(String title, String content, ButtonsType type, Alert.AlertType alertType) {
        this.title = title;
        this.content = content;
        this.buttonsType = type;
        this.alertType = alertType;

    }

    public void show() {

        if (alertType == Alert.AlertType.CONFIRMATION) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        } else if (alertType == Alert.AlertType.ERROR) {
            alert = new Alert(Alert.AlertType.ERROR);
        }


        Button button = new Button("Discord");

        alert.setTitle(title);
        alert.initOwner(Bootstrap.stage);
        alert.setHeaderText(content);

        alert.getDialogPane().getChildren().add(button);
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
            alert.setContentText("Plus d'info sur le discord");
            alert.getButtonTypes().setAll(okButton);
        }
        result = alert.showAndWait();

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
