package fr.modcraft.launcher.downloader;

import javafx.concurrent.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadTask extends Task<Void> {
    private String url;
    public static Path tempPath;

    public DownloadTask(String url) {
        this.url = url;

    }

    @Override
    protected Void call() throws Exception {
        URLConnection link = new URL(url).openConnection();
        tempPath = Files.createTempFile("java", ".exe");
        long fileLenght = link.getContentLengthLong();

        try (InputStream in = link.getInputStream();
             OutputStream out = Files.newOutputStream(tempPath)) {

            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
                nread += n;
                this.updateProgress(nread, fileLenght);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void failed() {
        System.out.println("failed.");

    }

    @Override
    public void succeeded() {
        System.out.println("java downloaded.");
        DownloaderManager.startJavaExe();

    }

    public static Path getTempPath() {
        return tempPath;
    }
}
