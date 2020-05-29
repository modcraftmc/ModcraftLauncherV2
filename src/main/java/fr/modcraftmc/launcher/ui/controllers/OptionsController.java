package fr.modcraftmc.launcher.ui.controllers;

import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class OptionsController {


    @FXML
    public Slider ramSlider;

    @FXML
    public CheckBox discordCheck;

    @FXML
    public CheckBox saveMeCheck;

    @FXML
    public AnchorPane container;




    public void setup() {

        ramSlider.setValue(ModcraftLauncher.settingsManager.getSettings().ram);
        discordCheck.setSelected(ModcraftLauncher.settingsManager.getSettings().discordRPC);
        saveMeCheck.setSelected(ModcraftLauncher.settingsManager.getSettings().keepLogin);

    }

    public int getRam() {

        return (int) ramSlider.valueProperty().get();
    }

    public void save() {
        ModcraftLauncher.settingsManager.getSettings().ram = getRam();
        ModcraftLauncher.settingsManager.save();

        Scene switchto = ModcraftApplication.mainScene;
        switchto.getStylesheets().add(ModcraftApplication.resourcesManager.getResource("global.css").toExternalForm());
        switchto.setFill(Color.TRANSPARENT);
        ModcraftApplication.window.setScene(switchto);
    }
}
