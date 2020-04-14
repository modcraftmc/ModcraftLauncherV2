package ma.forix.gmserver;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    private ServerSocket server = null;
    private ContentGenerator contentGenerator;
    private CommandReceiver commandReceiver;
    private final File gameDir = new File(System.getProperty("user.dir")+"/downloads/");
    private boolean isRunning = true;


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
            server = new ServerSocket(25667, 50);
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
                        System.out.println("["+TimeManager.getTime()+"] En attente d'une connexion...");
                        Socket client = server.accept();
                        System.out.println("["+ TimeManager.getTime()+"] New client attempt to connect...");
                        System.out.println("["+ TimeManager.getTime()+"] Client informations:");
                        System.out.println("    IP: "+ Arrays.toString(client.getInetAddress().getAddress()));
                        System.out.println("    Host Name: "+client.getInetAddress().getHostName());
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
