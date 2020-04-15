package fr.modcraftmc.launcher;

import javafx.application.Application;

import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
       Bootstrap.LOGGER.info("Starting system check...");
       //TODO: check system environement

       Bootstrap.LOGGER.info("Starting ModcraftLauncher bootstrap with args : " + Arrays.toString(args));
       Application.launch(Bootstrap.class, args);

    }

}
