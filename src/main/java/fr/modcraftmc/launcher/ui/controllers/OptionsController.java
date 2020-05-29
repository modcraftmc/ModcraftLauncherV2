package fr.modcraftmc.launcher.ui.controllers;

import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class OptionsController {

    @FXML
    public Slider ramS;

    @FXML
    public CheckBox discordrpc;

    @FXML
    public CheckBox saveme;

    @FXML
    public AnchorPane container;

    @FXML
    public Label ramLabel;

    public void setup() {
        ramS.setValue(ModcraftLauncher.settingsManager.getSettings().ram);
        ramLabel.setText(ramS.getValue() + "Gb");

        discordrpc.setSelected(ModcraftLauncher.settingsManager.getSettings().discordRPC);
        saveme.setSelected(ModcraftLauncher.settingsManager.getSettings().keepLogin);

        ramS.setMax(12);
        ramS.setMin(3);
        ramS.valueProperty().addListener(((observable, oldValue, newValue) -> {

            ramS.setValue(Math.round(newValue.intValue()));
            ramLabel.setText(Math.round(newValue.intValue()) + "Gb");
            System.out.println(newValue);
        }));

    }

    public int getRam() {
        return (int) ramS.getValue();

    }

    public void back() {
        ModcraftLauncher.settingsManager.getSettings().ram = (int) ramS.getValue();
        ModcraftLauncher.settingsManager.save();

        Scene switchto = ModcraftApplication.mainScene;
        switchto.getStylesheets().add(ModcraftApplication.resourcesManager.getResource("global.css").toExternalForm());
        switchto.setFill(Color.TRANSPARENT);
        ModcraftApplication.window.setScene(switchto);
    }
}
