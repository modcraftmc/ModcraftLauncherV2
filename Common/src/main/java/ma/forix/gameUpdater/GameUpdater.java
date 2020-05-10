package ma.forix.gameUpdater;

import fr.modcraftmc.modal.ModalBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class GameUpdater {

    public final static Logger LOGGER = LoggerFactory.getLogger("GameUpdater");

    private final String url;
    private final File gameDir;
    private final ProgressBar progressBar;
    private Task<Void> task;
    public static Thread update;
    public static EnumModcraft toDownload;
    public boolean deleter = true;

    //EXCEPTION HANDLER
    public static Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            //TODO: CRASH REPORTTER
            e.printStackTrace();
            update.interrupt();

        }
    };

    public static void checkServer() {
        try {
            Socket socket = new Socket("v1.modcraftmc.fr", 2121);
        } catch (IOException e) {
            ModalBuilder builder = new ModalBuilder();
            //builder.show();

        }
    }

    private final Downloader downloader;

    public GameUpdater(String url, File gameDir, ProgressBar bar){
        this.url = url;
        this.gameDir = gameDir;
        this.progressBar = bar;
        downloader = new Downloader(url, gameDir);
    }


    public void start(){
        if (deleter)
            downloader.deleter();

        update.start();
    }

    public void setDeleter(boolean deleter) {
        this.deleter = deleter;
    }

    public Task updater(){
        task = new Downloader(url, gameDir);
        Platform.runLater(() -> {
            if (progressBar != null) {
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());
            }
        });

        update = new Thread(task);
        update.setDaemon(true);
        update.setUncaughtExceptionHandler(exceptionHandler);
        return task;
    }

    public void stop(){
        update.interrupt();
    }

    public Task<Void> getTask(){
        return task;
    }

    public static void setToDownload(EnumModcraft file) {
        toDownload = file;
    }
}
