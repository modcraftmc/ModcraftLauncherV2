package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LiteUpdater {

    private final File localjar = new File(FilesManager.LAUNCHER_PATH, "bootstrap.jar");

    private final String url = "http://v1.modcraftmc.fr:100/beta/prelauncher/";
    private final String bootstrap = url + "bootstrap.jar";
    private final String md5 = url + "md5.txt";


    public LiteUpdater() {

    }

    public void update() {
        Prelauncher.LOGGER.info("staring liteupdater");

        if (!localjar.exists()) {
            downloadJar();
            launchJar();
        } else {
            if (checkJar(localjar)) {
                launchJar();
            } else {
                downloadJar();
                launchJar();
            }
        }

    }

    public boolean checkJar(File jar) {
        Prelauncher.LOGGER.info("checking jar");

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(md5).openConnection();
            if (con.getResponseCode() != 200) {
                return false;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String md5hash = bufferedReader.readLine();

            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            //Get the checksum
            String checksum = getFileChecksum(md5Digest, jar);

            return checksum.equals(md5hash);


        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void downloadJar() {
        Prelauncher.LOGGER.info("downloading jar");

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(bootstrap).openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(localjar);

            int count;
            double sumCount = 0.0;
            byte[] dataBuffer = new byte[1024];
            while ((count = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, count);

                sumCount += count;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void launchJar() {
        Prelauncher.LOGGER.info("launching jar");

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(localjar.getParentFile());
        builder.command("java", "-jar", "bootstrap.jar");
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }


        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }


}
