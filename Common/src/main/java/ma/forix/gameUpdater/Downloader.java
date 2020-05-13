package ma.forix.gameUpdater;

import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class Downloader extends Task<Void> {


    private String url;
    private final File gameDir;
    private JSONArray jsonArray, toDownload;
    private JSONObject object;
    private int downloadSize, bytesDownloaded, fileDownloaded, filesToDownload, threadsNumber;
    private final int SIMULTANEOUS = 20;
    private ArrayList<String> ignoreFiles, ignoreModId;
    private BufferedInputStream reader;
    private Thread updateBar;
    private final Os os;

    public static void main(String[] args) {
        new Downloader("http://v1.modcraftmc.fr:100/gameupdater/", new File("C:\\Users\\forix\\Desktop\\.modcraft\\"));
    }


    public Downloader(String url, File gameDir){
        GameUpdater.LOGGER.info("OS: "+System.getProperty("os.name"));
        if (System.getProperty("os.name").contains("Windows"))
            os = Os.WINDAUBE;
        else
            os = Os.UNIX;

        if (url.toCharArray()[url.toCharArray().length-1] == '/'){
            this.url = UrlAdapter(url);
        }
        this.gameDir = gameDir;
        if (!gameDir.exists()) gameDir.mkdir();

        long start;
        start = System.currentTimeMillis();
        GameUpdater.LOGGER.info("File to download : " + GameUpdater.toDownload.name());
        try {
            getContent(GameUpdater.toDownload.name());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        GameUpdater.LOGGER.info("getcontent time: "+(System.currentTimeMillis()-start));
        start = System.currentTimeMillis();
        getIgnoreList();
        GameUpdater.LOGGER.info("getIgnoreList time: "+(System.currentTimeMillis()-start));
        deleter();
    }

    private String getModId(File file){
        try {
            byte[] buffer = new byte[1 << 14];
            JarFile in = new JarFile(file);
            try {
                InputStream ein = in.getInputStream(in.getEntry("mcmod.info"));
                StringBuilder stringBuilder = new StringBuilder();
                for (int nr; 0 < (nr = ein.read(buffer)); ) {
                    stringBuilder.append(new String(buffer, 0, nr));
                }
                in.close();
                StringBuilder builder = new StringBuilder();
                for (String current : stringBuilder.toString().split("\n")){
                    if (current.contains("\"modid\"")){
                        builder = new StringBuilder(current);
                        for (char currentC : builder.toString().toCharArray()) {
                            if ((byte) currentC == 32 || (byte) currentC == 9 || (byte) currentC == 34 || (byte) currentC == 44) {
                                builder.deleteCharAt(builder.indexOf(String.valueOf(currentC)));
                            }
                        }
                    }
                }
                return builder.toString();
            } catch (NullPointerException e){
                e.printStackTrace();
                return null;
            }
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void getContent(String content) throws Exception {

            Socket socket = new Socket("v1.modcraftmc.fr", 2020);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedInputStream(socket.getInputStream());
            String reponse = null;
            jsonArray = new JSONArray();


            readContent(content);

            GameUpdater.LOGGER.info("content.json recovered");
            writer.write("close");
            writer.flush();
            writer.close();
            socket.close();
    }

    public void readContent(String content) {

        if (content == "LAUNCHER-CONTENT") content = "content";

        try (InputStreamReader streamReader = new InputStreamReader(new URL(this.url + "/" + content.toLowerCase() +".json").openStream())){
            Object obj = new JSONParser().parse(streamReader);
            jsonArray = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }



    private void getIgnoreList(){
        try(InputStreamReader streamReader = new InputStreamReader(new URL(this.url+"/ignore.txt").openStream())){
            int data = streamReader.read();
            StringBuilder reading = new StringBuilder();
            boolean writing = true;

            //Get ignore.txt content
            while (data != -1){
                if (writing) {
                    if (data == 13)
                        writing = false;
                    reading.append((char) data);
                } else
                    writing = true;
                data = streamReader.read();
            }

            //Splitting ignore.txt content in a string list
            String[] buffer;
            if (reading.toString().contains("\r"))
                buffer = reading.toString().split("\r");
            else
                buffer = reading.toString().split("\n");

            //Separating modid from ignored folders/files
            ignoreModId = new ArrayList<>();
            ignoreFiles = new ArrayList<>();
            for (String current : buffer){
                if (current.contains("modid:")){
                    ignoreModId.add(current/*new StringBuilder(current).delete(0, 6).toString()*/);
                } else {
                    ignoreFiles.add(current);
                }
            }

            //Display ignoreFiles content
            System.out.print("[");
            for (String current : ignoreFiles){
                System.out.print("\""+current+"\", ");
            }
            System.out.println("]");

            //Display ignoreModId content
            System.out.print("[");
            for (String current : ignoreModId){
                System.out.print("\""+current+"\", ");
            }
            System.out.println("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String UrlAdapter(String url){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < url.toCharArray().length-1; i++){
            sb.append(url.charAt(i));
        }
        return sb.toString();
    }


    public int GetDownloadSize(JSONArray toDownload){
        File cursor;
        for (Object array : toDownload){
            object = (JSONObject) array;
            cursor = new File(gameDir.toString() + "\\" + object.get("path").toString() + object.get("filename").toString());
            if (!cursor.exists()) downloadSize += Integer.parseInt(object.get("size").toString());
        }
        return downloadSize;
    }

    int fileNumber;
    int fileAnalyzed = 0;
    int simultane = 0;

    public void deleter(){

        long time1 = System.currentTimeMillis();
        File[] listing = FileUtils.listFiles(gameDir, null, true).toArray(new File[0]);
        long time2 = System.currentTimeMillis();
        GameUpdater.LOGGER.info("Listing time: "+(time2-time1));
        long temp = System.currentTimeMillis();
        Thread t = null;
        fileNumber = listing.length;
        GameUpdater.LOGGER.info("filenumber: "+fileNumber);
        for (File current : listing){
            t = new Thread(){
                @Override
                public void run() {
                    File cursor = current;
                    JSONObject obj;
                    super.run();
                    boolean ignore = false;
                    //Use MD5 algorithm
                    MessageDigest md5Digest = null;
                    try {
                        md5Digest = MessageDigest.getInstance("MD5");
                        //Get the checksum
                        String checksum = getFileChecksum(md5Digest, cursor);
                        for (Object array: jsonArray){
                            obj = (JSONObject) array;

                            if (checksum.equals(obj.get("md5").toString())) {
                                if (obj.get("filename").toString().equals(cursor.getName()))
                                    ignore = true;
                            }
                        }
                    } catch (NoSuchAlgorithmException | IOException e) {
                        e.printStackTrace();
                    }

                    if (cursor.getParent().equals(new File(gameDir, "mods"))){
                        for (String modid : ignoreModId){
                            try {
                                if (getModId(cursor).equals(modid)) {
                                    ignore = true;
                                }
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                    }

                    for (String now : ignoreFiles) {
                        if (cursor.toString().contains(now.replace("/", "\\"))) {
                            //System.out.println("[IGNORE LIST] This file is ignored: " + current.getName());
                            ignore = true;
                        }
                    }

                    if (!ignore) {
                        current.delete();
                        GameUpdater.LOGGER.info("[IGNORE LIST] Fichier '"+cursor+"' supprimé !");
                        //System.out.println("simultané: "+simultane);
                        simultane--;
                    } else
                        simultane--;
                    fileAnalyzed++;
                    //System.out.println("id: "+getId());
                }
            };
            simultane++;
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameUpdater.LOGGER.info("TOUT EST FINI");

        GameUpdater.LOGGER.info("[IGNORE LIST] time elapsed for deleting unecessary files: "+(System.currentTimeMillis()-temp));
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

    private void Verification(){
        filesToDownload = 0;
        File cursor;
        toDownload = new JSONArray();
        long temp = System.currentTimeMillis();
        MessageDigest md5Digest;
        boolean keep = false;

        for (Object array : jsonArray){
            object = (JSONObject) array;
            cursor = new File(gameDir.toString() + "\\" + object.get("path").toString() + object.get("filename").toString().replaceAll("#var#", ".var"));

            if (!cursor.exists()) {
                toDownload.add(object);
                filesToDownload++;
            } else {
                try {
                    md5Digest = MessageDigest.getInstance("MD5");
                    keep = false;
                    for (String now : ignoreFiles){

                        if (now.contains("/")){
                            now = now.replace("/", "\\");
                        }
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
        GameUpdater.LOGGER.info("[VERIFICATION] temps écoulé vérif: "+(System.currentTimeMillis()-temp));
        GameUpdater.LOGGER.info("[VERIFICATION] Download size: "+GetDownloadSize(toDownload)/1024+"Ko");
        GameUpdater.LOGGER.info("[VERIFICATION] Files to download: "+toDownload);
    }

    private void download(File cursor, JSONObject obj) {
        Thread download = new Thread(() -> {
            String path = obj.get("path").toString();
            String fileName = obj.get("filename").toString();
            try {
                threadsNumber++;
                URL fileUrl;
                if (GameUpdater.toDownload.name().equalsIgnoreCase("launcher-content")) {
                    fileUrl = new URL(this.url+"/downloads/" + path.replace("\\", "/").replaceAll(" ", "%20").replaceAll("#", "%23") + fileName.replaceAll(" ", "%20").replaceAll("#", "%23"));
                } else {
                    fileUrl = new URL(this.url + path.replace("\\", "/").replaceAll(" ", "%20").replaceAll("#", "%23") + fileName.replaceAll(" ", "%20").replaceAll("#", "%23"));
                }

                System.out.println("[GameUpdater] Téléchargement du fichier: "+ fileUrl.toString());
                BufferedInputStream bis = new BufferedInputStream(fileUrl.openStream());
                FileOutputStream fos = new FileOutputStream(new File(cursor.toString().replaceAll("#var#", ".var")));
                final byte[] data = new byte[64];
                int count;
                while ((count = bis.read(data, 0, 32)) != -1) {
                    bytesDownloaded += count;
                    updateProgress(bytesDownloaded, downloadSize);
                    fos.write(data, 0, count);
                }
                threadsNumber--;
                fileDownloaded++;
                GameUpdater.LOGGER.info("[GameUpdater] Téléchargement du fichier terminé :"+fileName);
                bis.close();
                fos.flush();
                fos.close();
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

    private long time;

    @Override
    protected Void call() throws Exception {
        File cursor;
        Verification();
        threadsNumber = 0;
        time = System.currentTimeMillis()/1000;

        updateBar = new Thread(){
            @Override
            public void run() {
                super.run();
                updateProgress(bytesDownloaded, downloadSize);
            }
        };
        updateBar.start();

        for (Object array : toDownload){
            object = (JSONObject) array;

            String path = object.get("path").toString().replace("\\", "/");
            cursor = new File(gameDir.toString() + "/" + path.replace("\\", "/") + object.get("filename").toString());
            if (cursor.getParentFile().exists()) {
                    if (!cursor.exists()) {
                        download(cursor, object);
                    }
            } else {
                cursor.getParentFile().mkdirs();
                download(cursor, object);
            }
        }

        boolean finished = false;
        while (!finished){

            if (fileDownloaded >= filesToDownload)
                finished = true;
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
        GameUpdater.LOGGER.info("[GameUpdater] Downloading time: "+(System.currentTimeMillis()/1000-time)+" sec");
        GameUpdater.LOGGER.info("[GameUpdater] Update finished !");
        super.succeeded();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
    }
}
