package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;
import net.jimmc.jshortcut.JShellLink;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

public class Prelauncher {

    public final static Logger LOGGER = LoggerFactory.getLogger("ModcraftMC-prelauncher");
    public static JShellLink link;
    public static String filepath = FilesManager.LAUNCHER_PATH + "bootsrap.jar";

    public static void main(String[] args) {

        LiteUpdater updater = new LiteUpdater();

        try {
            link = new JShellLink();

        } catch (Exception e) {

        }

        updater.update();
        createDesktopShortcut();

    }

    public static void createDesktopShortcut() {

        try {
            link.setFolder(JShellLink.getDirectory("desktop"));
            link.setName("ModcraftLauncher");
            link.setPath(filepath);
            link.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
