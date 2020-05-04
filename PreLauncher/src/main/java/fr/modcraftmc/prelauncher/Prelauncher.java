package fr.modcraftmc.prelauncher;

import fr.modcraftmc.FilesManager;

import javax.swing.*;
import java.io.File;

public class Prelauncher {

    FilesManager filesManager = new FilesManager();

    public static void main(String[] args) {
        Panel panel;
        JFrame frame = new JFrame("ModcraftLauncher");
        frame.setLayout(null);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel = new Panel());
        frame.setResizable(false);
        frame.setVisible(true);

        panel.load();

    }

    public void check() {

        File javacheck = new File(filesManager.getJavaPath(), "java");

        if (filesManager.getJavaPath().isDirectory()) {

            if (javacheck.exists()) {

            }
        }

    }
}
