package fr.modcraftmc.launcher.libs.downloader;

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
    private Thread update;
    private boolean delete = true;
    public static EnumModcraft toDownload;

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
        if (delete)
            downloader.Suppresser();
        update.start();
    }

    public void Suppresser(boolean value){
        delete = value;
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
