package ma.forix.gameUpdater.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import ma.forix.gameUpdater.GameUpdater;
import ma.forix.gameUpdater.Os;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DownloadTask extends Task<Void> {

    private int downloadSize, bytesDownloaded, fileDownloaded, filesToDownload, threadsNumber, bytesTodownload;

    //INIT SETTINGS
    public final Os operatingSystem;
    public final String serverUrl;
    public final File directory;
    public final ProgressBar progressBar;
    private final Label label;

    //REMOTE CONTENT
    public JSONArray remoteContent, toDownload;
    public List<String> ignoreList;

    public DownloadTask(String serverUrl, File directory, ProgressBar progressBar, Label label) {
        String os = System.getProperty("os.name");
        GameUpdater.LOGGER.info("OS: " + os);


        if (os.contains("Windows")) operatingSystem = Os.WINDAUBE; else operatingSystem = Os.UNIX;

        this.serverUrl = serverUrl;
        this.directory = directory;
        this.progressBar = progressBar;
        this.label = label;
        if (!directory.exists()) directory.mkdir();

        GameUpdater.LOGGER.info("Starting updater");

        GameUpdater.LOGGER.info("Fetching remote content");
        remoteContent = getRemoteContent();

        GameUpdater.LOGGER.info("Fetching remote ignorelist");
        ignoreList = getIgnoreList();
        System.out.println(ignoreList);


    }

    public JSONArray getRemoteContent() {

        try (InputStreamReader streamReader = new InputStreamReader(new URL(this.serverUrl + "/content.json").openStream())) {
            Object content = new JSONParser().parse(streamReader);
            return (JSONArray) content;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getIgnoreList() {
        ignoreList = new ArrayList<>();

        try (InputStreamReader streamReader = new InputStreamReader(new URL(this.serverUrl + "/ignore.txt").openStream())) {

            int list = streamReader.read();
            StringBuilder readed = new StringBuilder();
            boolean writing = true;

            while (list != -1){
                if (writing) {
                    if (list == 13)
                        writing = false;
                    readed.append((char) list);
                } else
                    writing = true;
                list = streamReader.read();
            }

            String[] buffer;
            if (readed.toString().contains("\r"))
                buffer = readed.toString().split("\r");
            else
                buffer = readed.toString().split("\n");

            return Arrays.asList(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    int fileAnalyzed = 0;
    int simultane = 0;
    public void deleter() {

        Collection<File> localFileList = FileUtils.listFiles(directory, null, true);

        Thread thread = null;

        for (File current : localFileList) {

            thread = new Thread(() -> {

                File cursor = current;

                boolean ignore = false;
                MessageDigest md5Digest = null;
                try {
                        md5Digest = MessageDigest.getInstance("MD5");
                        String checkSum = getFileChecksum(md5Digest, current);

                        for (Object localFile : remoteContent) {
                            JSONObject properties = (JSONObject) localFile;
                            String md5 = (String) properties.get("md5");

                            if (checkSum.equals(md5)) ignore = true;
                        }

                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }

                for (String now : ignoreList) {
                    if (cursor.toString().contains(now.replace("/", "\\"))) {
                        GameUpdater.LOGGER.info("[IGNORE LIST] This file is ignored: " + current.getName());
                        ignore = true;
                    }
                }

                if (!ignore) {
                    current.delete();
                    GameUpdater.LOGGER.info("[IGNORE LIST] Fichier '"+cursor+"' supprimé !");
                    --simultane;
                } else {
                    --simultane;
                }
                fileAnalyzed++;
            });
            simultane++;

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            GameUpdater.LOGGER.info("current file : " + current.getName());

            Platform.runLater(()-> label.setText("Analyse des fichiers en cours... " + (int)(this.progressProperty().getValue() * 100) + "%"
                    + " fichier " + fileAnalyzed + " sur " + localFileList.size()));

            this.updateProgress(fileAnalyzed, localFileList.size());

        }



        GameUpdater.LOGGER.info("TOUT EST FINI");

    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public void verification() {
        fileDownloaded = 0;
        File cursor;
        toDownload = new JSONArray();

        MessageDigest md5Digest;
        boolean keep = false;

        for (Object array : remoteContent){
            JSONObject object = (JSONObject) array;
            cursor = new File(directory + "\\" + object.get("path").toString() + object.get("filename").toString().replaceAll("#var#", ".var"));

            if (!cursor.exists()) {
                toDownload.add(object);
                filesToDownload++;
            } else {
                try {
                    md5Digest = MessageDigest.getInstance("MD5");
                    keep = false;
                    for (String now : ignoreList){

                        if (cursor.toString().contains(now)){
                            keep = true;
                        }
                    }
                    if (!getFileChecksum(md5Digest, cursor).equals(object.get("md5")) && !keep){
                        //cursor.delete();
                        toDownload.add(object);
                        filesToDownload++;
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
       // GameUpdater.LOGGER.info("[VERIFICATION] temps écoulé vérif: "+(System.currentTimeMillis()-temp));
       // GameUpdater.LOGGER.info("[VERIFICATION] Download size: "+GetDownloadSize(toDownload)/1024+"Ko");
        GameUpdater.LOGGER.info("[VERIFICATION] Files to download: "+ toDownload);
        bytesTodownload = GetDownloadSize(toDownload);

    }

    private final int SIMULTANEOUS = 20;
    private void download(File cursor, JSONObject obj) {

        Thread download = new Thread(() -> {
            String path = obj.get("path").toString();
            String fileName = obj.get("filename").toString();
            try {
                threadsNumber++;
                URL fileUrl;
                    fileUrl = new URL(this.serverUrl+"/downloads/" + path.replace("\\", "/").replaceAll(" ", "%20").replaceAll("#", "%23") + fileName.replaceAll(" ", "%20").replaceAll("#", "%23"));


                System.out.println("Téléchargement du fichier: "+ fileUrl);
                BufferedInputStream bis = new BufferedInputStream(fileUrl.openStream());
                FileOutputStream fos = new FileOutputStream(new File(cursor.toString().replaceAll("#var#", ".var")));
                final byte[] data = new byte[1024];
                int count;
                while ((count = bis.read(data, 0, 32)) != -1) {
                    bytesDownloaded += count;
                    fos.write(data, 0, count);
                }
                threadsNumber--;
                bis.close();
                fos.flush();
                fos.close();
                fileDownloaded++;
                GameUpdater.LOGGER.info("Téléchargement du fichier terminé :"+fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        download.setUncaughtExceptionHandler(GameUpdater.exceptionHandler);
        download.start();
        if (threadsNumber > SIMULTANEOUS) {
            try {
                download.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public int GetDownloadSize(JSONArray toDownload){
        File cursor;
        for (Object array : toDownload){
            JSONObject object = (JSONObject) array;
            cursor = new File(directory.toString() + "\\" + object.get("path").toString() + object.get("filename").toString());
            if (!cursor.exists()) downloadSize += Integer.parseInt(object.get("size").toString());
        }
        return downloadSize;
    }

    boolean finished = false;

    @Override
    protected Void call() {


        Thread updateBar = null;

        File cursor;
        deleter();
        verification();
        threadsNumber = 0;


        updateBar = new Thread(() -> {
            while (!finished)
                this.updateProgress(bytesDownloaded, bytesTodownload);
        });
        updateBar.start();


        for (Object array : toDownload){
            JSONObject object = (JSONObject) array;

            String path = object.get("path").toString().replace("\\", "/");
            cursor = new File(directory.toString() + "/" + path.replace("\\", "/") + object.get("filename").toString());

            if (cursor.getParentFile().exists()) {
                Platform.runLater(()-> label.setText("Téléchargement en cours... " + (int)(this.progressProperty().getValue() * 100) + "%"
                        + " fichier " + fileDownloaded + " sur " + toDownload.size()));
                if (!cursor.exists()) {
                    download(cursor, object);
                }
            } else {
                cursor.getParentFile().mkdirs();
                download(cursor, object);
            }


        }

        while (!finished){

            if (fileDownloaded >= toDownload.size()){

                finished = true;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void succeeded() {
        this.updateProgress(100, 100);
        //GameUpdater.LOGGER.info("[GameUpdater] Downloading time: "+(System.currentTimeMillis()/1000-time)+" sec");
        GameUpdater.LOGGER.info("[GameUpdater] Update finished !");
        super.succeeded();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
    }
}
