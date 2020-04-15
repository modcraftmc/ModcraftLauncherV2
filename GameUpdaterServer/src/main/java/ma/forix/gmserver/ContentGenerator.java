package ma.forix.gmserver;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ContentGenerator extends Thread implements Runnable {

    private static JSONArray content;
    private static JSONObject obj;
    private static File[] gameContent;
    private static File gameDir, timeFile, contentFile, bootstrapFile, prelauncherFile;
    private static ArrayList<String> scheduledTime;

    public ContentGenerator(File gameDir){
        this.gameDir = gameDir;
        if (!gameDir.exists())
            gameDir.mkdir();
        content = new JSONArray();
        timeFile = new File(System.getProperty("user.dir")+"/Scheduling.txt");
        if (!timeFile.exists()) {
            try {
                timeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentFile = new File(System.getProperty("user.dir")+"/content.json");
        if (timeFile.exists())
            loadTimeFile();
        else
            System.err.println("["+TimeManager.getTime()+"] Le fichier de configuration des heures de vérification des dossiers du jeu n'existe pas !");
    }

    private boolean alreadyGenerated = false;

    @Override
    public void run() {
        super.run();
        String previousTime = "";
        while(isAlive()){
            String currentTime;
            if (TimeManager.getMinute() < 10)
                currentTime = TimeManager.getHour()+":0"+TimeManager.getMinute();
            else
                currentTime = TimeManager.getHour()+":"+TimeManager.getMinute();

            if (!(previousTime.equals(currentTime))){
                previousTime = currentTime;
                alreadyGenerated = false;
            }

            for (String timeCursor : scheduledTime){
                if (timeCursor.equals(currentTime) && !alreadyGenerated){
                    System.out.println("["+TimeManager.getTime()+"] Game folder scan started");
                    generate();
                    System.out.println("["+TimeManager.getTime()+"] Game folder scan finished");
                    alreadyGenerated = true;
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadTimeFile(){
        scheduledTime = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(timeFile))) {
            String line;
            System.out.println("["+TimeManager.getTime()+"] Reloading Scheduling time file...");
            while ((line = br.readLine()) != null) {
                System.out.println("    New scheduled hour: "+line);
                scheduledTime.add(line);
            }
            System.out.println("["+TimeManager.getTime()+"] Reloading process finished !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generate(){
        gameContent = FileUtils.listFiles(gameDir, null, true).toArray(new File[0]);
        content = new JSONArray();
        for (File cursor: gameContent){
            obj = new JSONObject();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getParentFile().toString().length(); i++){
                if (i >= gameDir.toString().length()+1){
                    builder.append(cursor.getParentFile().toString().charAt(i));
                }
            }
            builder.append("\\");
            String path = builder.toString();
            obj.put("path", path);
            obj.put("filename", cursor.getName());
            //Use MD5 algorithm
            try {
                MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                //Get the checksum
                String checksum = getFileChecksum(md5Digest, cursor);
                obj.put("md5", checksum);
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
            obj.put("size", cursor.length());
            content.add(obj);
        }
        try {
            FileWriter fileWriter = new FileWriter(contentFile);
            fileWriter.write(content.toJSONString());
            fileWriter.flush();
            fileWriter.close();
            ServerThread.contentCache = ServerThread.readContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

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
}
