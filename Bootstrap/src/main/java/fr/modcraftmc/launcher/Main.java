package fr.modcraftmc.launcher;

import javafx.application.Application;

import javax.swing.*;
import java.util.Arrays;

public class Main {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        System.out.println("starting system check");
        if (!hasJAVAFX()) {
            JFrame frame = new JFrame("erreur");
            JOptionPane.showMessageDialog(frame, "Java fx n'est pas installé");
            if (isLinux()) {
                System.out.println("linux detected, installing...");
            } else {
                System.exit(1);
            }
        }

        System.out.println("Starting ModcraftLauncher bootstrap with args : " + Arrays.toString(args));
        Application.launch(Bootstrap.class, args);

    }

    public static boolean isLinux() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0 );
    }


    public static boolean hasJAVAFX() {
        boolean hasJavaFX = false;
        try {
            hasJavaFX = true;
            Class.forName("javafx.application.Application");
        } catch (Exception e) {
            hasJavaFX = false;
            e.printStackTrace();
        } finally {
            if (hasJavaFX) {
                System.out.println("JavaFX is installed");
            } else {
                System.out.println("JavaFX is not installed, abording");
            }
            return hasJavaFX;
        }
    }
}
