package ma.forix.gameUpdater;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;

public class GameUpdater {

    private String url;
    private File gameDir;
    private ProgressBar progressBar;
    private Label label;
    private Task<Void> task;
    public static Thread update;
    public static EnumModcraft toDownload;

    //EXCEPTION HANDLER
    public static Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            //TODO: CRASH REPORTTER
            update.interrupt();

        }
    };

    private Downloader downloader;

    public GameUpdater(String url, File gameDir, ProgressBar bar, Label label){
        this.url = url;
        this.gameDir = gameDir;
        this.progressBar = bar;
        this.label = label;
        downloader = new Downloader(url, gameDir);
    }

    public GameUpdater(String url, File gameDir) {
        this.url = url;
        this.gameDir = gameDir;
        downloader = new Downloader(url, gameDir);
    }

    public void start(){
        downloader.deleter();
        update.start();
    }


    public Task updater(){
        task = new Downloader(url, gameDir);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());
            }
        });

        update = new Thread(task);
        update.setDaemon(true);
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
