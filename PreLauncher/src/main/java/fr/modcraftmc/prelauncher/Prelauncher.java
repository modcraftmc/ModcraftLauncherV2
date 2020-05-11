package fr.modcraftmc.prelauncher;

import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

public class Prelauncher {

    public final static Logger LOGGER = LoggerFactory.getLogger("ModcraftMC-prelauncher");

    public static void main(String[] args) {

        LiteUpdater updater = new LiteUpdater();

        updater.update();

    }

}
