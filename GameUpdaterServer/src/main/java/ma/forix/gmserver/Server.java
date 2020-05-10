package ma.forix.gmserver;


import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server = null;
    private ContentGenerator contentGenerator;
    private CommandReceiver commandReceiver;
    private final File gameDir = new File(System.getProperty("user.dir")+"/downloads/");
    private final boolean isRunning = true;
    public final static Logger LOGGER = LoggerFactory.getLogger("GameUpdaterServer");


    public static void main(String[] args) {
        new TimeManager();
        Server server = new Server();
        server.open();
        try {
            File ignoreList = new File(System.getProperty("user.dir")+"/ignore.txt");
            if (!ignoreList.exists())
                ignoreList.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server(){
        try {
            server = new ServerSocket(2020, 50);
            contentGenerator = new ContentGenerator(gameDir);
            commandReceiver = new CommandReceiver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open(){
        Thread opening = new Thread(){
            @Override
            public void run() {
                super.run();
                while (isRunning){
                    try {
                        Server.LOGGER.info("En attente d'une connexion...");
                        Socket client = server.accept();
                        Server.LOGGER.info("New client attempt to connect...");
                        Server.LOGGER.info("Client informations:");
                        Server.LOGGER.info("    IP: "+client.getInetAddress().getHostName());
                        Thread thread = new Thread(new ServerThread(client));
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    server = null;
                }
            }
        };
        opening.start();
        contentGenerator.start();
        commandReceiver.start();
    }
}
