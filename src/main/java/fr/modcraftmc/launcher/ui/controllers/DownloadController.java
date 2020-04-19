package fr.modcraftmc.launcher.ui.controllers;

import fr.modcraftmc.launcher.core.ModcraftLauncher;
import javafx.fxml.FXML;
import javafx.scene.media.MediaView;
import ma.forix.gameUpdater.EnumModcraft;
import ma.forix.gameUpdater.GameUpdater;

import java.io.File;

public class DownloadController {

    @FXML
    public MediaView media;

    public static final File SKYBLOCK = new File(ModcraftLauncher.filesManager.getInstancesPath(), "skyblock");

    public void download() {
        Thread downloadThread;

        downloadThread = new Thread(() -> {

            GameUpdater.setToDownload(EnumModcraft.LAUNCHER);
            GameUpdater gameUpdater = new GameUpdater("http://v1.modcraftmc.fr:100/gameupdater/", SKYBLOCK);
            gameUpdater.setDeleter(true);
            gameUpdater.start();
        });
        downloadThread.start();

    }
}
