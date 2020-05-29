package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

public class Prelauncher {

    public final static Logger LOGGER = LoggerFactory.getLogger("ModcraftMC-prelauncher");
    public static String filepath = FilesManager.LAUNCHER_PATH + "bootsrap.jar";

    public static void main(String[] args) {

        LiteUpdater updater = new LiteUpdater();


        updater.update();

    }

}
