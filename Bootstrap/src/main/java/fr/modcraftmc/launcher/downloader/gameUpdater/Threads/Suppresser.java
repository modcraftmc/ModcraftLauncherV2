package fr.modcraftmc.launcher.downloader.gameUpdater.Threads;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class Suppresser extends Thread implements Runnable {

    private File gameFile;
    private JSONObject object;
    private JSONArray jsonArray;
    private ArrayList<String> ignoreModId, ignoreFiles;

    public Suppresser(File gameFile, JSONArray jsonArray, ArrayList<String> ignoreModId, ArrayList<String> ignoreFiles){
        this.gameFile = gameFile;
        this.jsonArray = jsonArray;
        this.ignoreModId = ignoreModId;
        this.ignoreFiles = ignoreFiles;
    }

    @Override
    public void run() {
        super.run();
        boolean ignore = false;
        //Use MD5 algorithm
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
            //Get the checksum
            String checksum = getFileChecksum(md5Digest, gameFile);
            for (Object array: jsonArray){
                object = (JSONObject) array;

                if (checksum.equals(object.get("md5").toString())) {
                    System.out.println("file md5: "+checksum);
                    System.out.println("object md5: "+object.get("md5").toString());
                    if (object.get("filename").toString().equals(gameFile.getName()))
                        ignore = true;
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        if (gameFile.getParent().equals(System.getProperty("user.home")+"\\AppData\\Roaming\\.modcraft\\mods")){
            for (String modid : ignoreModId){
                try {
                    if (getModId(gameFile).equals(modid)) {
                        ignore = true;
                    }
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }

        for (String now : ignoreFiles) {
            if (gameFile.toString().contains(now.replace("/", "\\"))) {
                //System.out.println("[IGNORE LIST] This file is ignored: " + current.getName());
                ignore = true;
            }
        }

        if (!ignore) {
            //current.delete();
            System.out.println("[IGNORE LIST] Fichier '"+gameFile+"' supprimé !");
        }
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
}
