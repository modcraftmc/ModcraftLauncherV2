package fr.modcraftmc.launcher.ui.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.modcraftmc.launcher.ui.ModcraftApplication;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ma.forix.gameUpdater.EnumModcraft;
import ma.forix.gameUpdater.GameUpdater;

import java.io.File;

import static java.lang.Thread.sleep;

public class DownloadController {

    @FXML
    public ProgressBar progressBar;

    @FXML
    public ImageView imageview;

    @FXML
    public AnchorPane container;


    public static final File SKYBLOCK = new File(ModcraftLauncher.filesManager.getInstancesPath(), "skyblock");

    public void download() {

        Thread update = new Thread(() -> {

            imageview.setSmooth(true);
            imageview.setPreserveRatio(true);
            imageview.setImage(new Image("http://getwallpapers.com/wallpaper/full/2/9/a/90106.jpg"));

            GameUpdater.setToDownload(EnumModcraft.LAUNCHER);
            GameUpdater gameUpdater = new GameUpdater("http://v1.modcraftmc.fr:100/beta/", SKYBLOCK, progressBar);

            gameUpdater.updater().setOnSucceeded(event -> {
                new Thread(() -> {
                    launch();

                }).start();

            });

            gameUpdater.setDeleter(false);
            gameUpdater.start();

        });
        update.start();
    }

    public void launch() {
        try {
            GameVersion VERSION = new GameVersion("1.15.2", GameType.V_1_15_2_FORGE);
            GameInfos INFOS = new GameInfos("modcraftmc", new File(ModcraftLauncher.filesManager.getInstancesPath(), "skyblock"), VERSION, new GameTweak[]{});

            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, Authenticator.authInfos);
            profile.getVmArgs().add("-Xmx8G");
            ExternalLauncher launcher = new ExternalLauncher(profile);
            Process p = null;
            try {
                p = launcher.launch();
                hide();
                sleep(5000);
                p.waitFor();
            } catch (InterruptedException | LaunchException e) {
                e.printStackTrace();
            }
        } catch (Exception ignored) {} finally {
            System.exit(0);
        }
    }

    public void hide() {

        AnimationFX close = new FadeOut(container);
        close.setSpeed(5D);
        close.setResetOnFinished(true);
        close.play();
        close.setOnFinished(event -> ModcraftApplication.window.setIconified(true));
    }

}
