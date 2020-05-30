package fr.modcraftmc.launcher.ui.controllers;

import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

import java.lang.management.ManagementFactory;

public class OptionsController {


    @FXML
    public Slider ramSlider;

    @FXML
    public CheckBox discordCheck;

    @FXML
    public CheckBox saveMeCheck;

    @FXML
    public CheckBox ramCheck;

    @FXML
    public AnchorPane container;




    public void setup() {

        ramCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                calculateRam();
                ramSlider.setDisable(true);
            } else {
                ramSlider.setDisable(false);
            }

        });

        discordCheck.setSelected(ModcraftLauncher.settingsManager.getSettings().discordRPC);
        saveMeCheck.setSelected(ModcraftLauncher.settingsManager.getSettings().keepLogin);
        ramCheck.setSelected(ModcraftLauncher.settingsManager.getSettings().autoram);


        if (ModcraftLauncher.settingsManager.getSettings().autoram) calculateRam();
        ramSlider.setValue(ModcraftLauncher.settingsManager.getSettings().ram);

    }

    public void calculateRam() {
        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        double ram = (double) Math.round(getSize(memorySize));
        if (ram < 4) {
            ramSlider.setMax(ram);
            ramSlider.setValue(ram);
        }

        if (ram > 6) {
            ramSlider.setMax(ram);
            ram = ram / 100 * 75;
            ramSlider.setValue(ram);
            ModcraftLauncher.settingsManager.getSettings().ram = (int) ram;

        }
    }

    public static double getSize(long size) {
         double s= 0;
        double kb = size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;
        if(size < 1024L) {
            s = size;
        } else if(size >= 1024 && size < (1024L * 1024)) {
            s =  kb;
        } else if(size >= (1024L * 1024) && size < (1024L * 1024 * 1024)) {
            s = mb;
        } else if(size >= (1024L * 1024 * 1024) && size < (1024L * 1024 * 1024 * 1024)) {
            s = gb;
        } else if(size >= (1024L * 1024 * 1024 * 1024)) {
            s = tb;
        }
        return s;
    }

    public int getRam() {

        return (int) ramSlider.valueProperty().get();
    }

    public void save() {

        ModcraftLauncher.settingsManager.getSettings().autoram = ramCheck.isSelected();
        ModcraftLauncher.settingsManager.getSettings().ram = getRam();
        ModcraftLauncher.settingsManager.getSettings().keepLogin = saveMeCheck.isSelected();
        ModcraftLauncher.settingsManager.save();

        ModcraftApplication.getInstance().switchToMain();
    }
}
