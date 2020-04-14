package fr.modcraftmc.launcher;

import javafx.application.Application;

import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
        System.out.println("starting system check");

        System.out.println("Starting ModcraftLauncher bootstrap with args : " + Arrays.toString(args));
        Application.launch(Bootstrap.class, args);

    }

}
