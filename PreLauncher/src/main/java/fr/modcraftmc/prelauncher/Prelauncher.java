package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;
import ma.forix.gameUpdater.EnumModcraft;
import ma.forix.gameUpdater.GameUpdater;

public class Prelauncher {

    private static final FilesManager filesManager = new FilesManager();

    public static void main(String[] args) {

        Thread update = new Thread(() -> {


            GameUpdater.setToDownload(EnumModcraft.PRELAUNCHER);
            GameUpdater gameUpdater = new GameUpdater("http://v1.modcraftmc.fr:100/beta/", filesManager.getLauncherPath());

            gameUpdater.updater().setOnSucceeded(event -> new Thread(Prelauncher::launch).start());

            gameUpdater.start();

        });
        update.start();

    }

    public static void launch() {

    }

}
