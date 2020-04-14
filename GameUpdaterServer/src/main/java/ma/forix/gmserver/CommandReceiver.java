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
                    System.out.println("["+TimeManager.getTime()+"] Generating content.json");
                    ContentGenerator.generate();
                    System.out.println("["+TimeManager.getTime()+"] content.json file generated !");
                    break;
                case "RELOAD":
                    System.out.println("["+TimeManager.getTime()+"] Reloading schedule time file...");
                    ContentGenerator.loadTimeFile();
                    System.out.println("["+TimeManager.getTime()+"] Schedule time reloaded !");
                    break;
                case "HELP":
                    System.out.println("["+TimeManager.getTime()+"] Command list:");
                    System.out.println("    GENERATE : Scan game folder to create content.json file \n which contains downloads subfiles information");
                    System.out.println("    RELOAD : Reload Scheduled Time.txt file which contains \n in each line the time of when game folder will be scanned");
                    break;
                case "SHUTDOWN":
                    System.out.println("["+TimeManager.getTime()+"] Closing the server.");
                    System.exit(0);
                    break;
                default:
                    System.err.println("["+TimeManager.getTime()+"] Unknown command");
                    break;
            }
        }
    }
}
