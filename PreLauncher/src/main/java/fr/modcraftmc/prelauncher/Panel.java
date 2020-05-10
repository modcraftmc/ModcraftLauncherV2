package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Panel extends JPanel {

    JProgressBar bar = new JProgressBar();
    Label label = new Label("instalation des prérequis");


    public Panel() {
        this.setLayout(null);

        bar.setBounds(0, 100, 300, 21);


        this.add(bar);
    }

    public void load() {

        try {
            BufferedInputStream in = null;
            FileOutputStream out = null;
            Path javaPath = Files.createTempFile("java", ".modcraft");
            File java = javaPath.toFile();

            URL url = new URL("http://v1.modcraftmc.fr/dl/java.zip");
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();


            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(java);
            byte[] data = new byte[1024];
            int count;
            double sumCount = 0.0;

            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);

                sumCount += count;
                if (size > 0) {
                    this.bar.setMaximum(100);
                    this.bar.setValue((int)(sumCount / size * 100.0));
                }
            }

            ZipInputStream ina = new ZipInputStream(new FileInputStream(java));

            ZipEntry entry = ina.getNextEntry();

            // Open the output file

            OutputStream outa = new FileOutputStream(FilesManager.JAVA_PATH);

            // Transfer bytes from the ZIP file to the output file
            byte[] buf = new byte[1024];

            int len;
            while ((len = ina.read(buf)) > 0) {
                outa.write(buf, 0, len);
            }

            // Close the streams
            outa.close();
            ina.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
