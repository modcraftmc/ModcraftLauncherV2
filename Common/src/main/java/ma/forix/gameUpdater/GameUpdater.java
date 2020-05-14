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
    private ProgressBar progressBar;
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


    public GameUpdater(String url, File gameDir, ProgressBar bar){
        this.url = url;
        this.gameDir = gameDir;
        this.progressBar = bar;
    }

    public GameUpdater(String url, File gameDir){
        this.url = url;
        this.gameDir = gameDir;

    }


    public void start(){

        update.start();
    }

    public void setDeleter(boolean deleter) {
        this.deleter = deleter;
    }

    public Task updater(){
        task = new Updater(url, gameDir, progressBar);
        if (progressBar != null) {
            Platform.runLater(() -> {
                     progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());
            });

        }

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
