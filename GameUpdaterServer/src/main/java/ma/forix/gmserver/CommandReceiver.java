package ma.forix.gmserver;

import java.util.Scanner;

public class CommandReceiver extends Thread implements Runnable {

    private Scanner input;

    public CommandReceiver(){
        input = new Scanner(System.in);
    }

    @Override
    public void run() {
        super.run();
        while (isAlive()){
            String command = input.next();
            switch (command.toUpperCase()){
                case "GENERATE":
                    Server.LOGGER.info("Generating content.json");
                    ContentGenerator.generate();
                    Server.LOGGER.info("content.json file generated !");
                    break;
                case "RELOAD":
                    Server.LOGGER.info("Reloading schedule time file...");
                    ContentGenerator.loadTimeFile();
                    Server.LOGGER.info("Schedule time reloaded !");
                    break;
                case "HELP":
                    Server.LOGGER.info("[Command list:");
                    Server.LOGGER.info("    GENERATE : Scan game folder to create content.json file \n which contains downloads subfiles information");
                    Server.LOGGER.info("    RELOAD : Reload Scheduled Time.txt file which contains \n in each line the time of when game folder will be scanned");
                    break;
                case "SHUTDOWN":
                    Server.LOGGER.info("Closing the server.");
                    System.exit(0);
                    break;
                default:
                    Server.LOGGER.info("Unknown command");
                    break;
            }
        }
    }
}
