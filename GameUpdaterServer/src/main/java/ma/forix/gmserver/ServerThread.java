package ma.forix.gmserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class ServerThread extends Thread implements Runnable {

    private final Socket client;
    private PrintWriter writer;
    private BufferedInputStream reader;
    private String prelauncherJson, bootstrapJson;

    public static String contentCache = readContent();

    public ServerThread(Socket client){
        this.client = client;

    }

    @Override
    public void run() {
        super.run();
        System.out.println("["+TimeManager.getTime()+"] ["+getName()+"] Connection established !");
        while (!client.isClosed()) {
            try {
                writer = new PrintWriter(client.getOutputStream());
                reader = new BufferedInputStream(client.getInputStream());
                String reponse = read();
                Server.LOGGER.info("New input from client received: "+reponse);
                switch (reponse){
                    case "getContent":
                        writer.write(contentCache);
                        writer.flush();
                        writer.close();
                        break;
                    case "getContent-LAUNCHER":
                        writer.write(contentCache);
                        writer.flush();
                        writer.close();
                        break;
                    case "getContent-BOOTSTRAP":
                        writer.write(bootstrapJson);
                        writer.flush();
                        writer.close();
                        break;
                    case "getContent-PRELAUNCHER":
                        writer.write(prelauncherJson);
                        writer.flush();
                        writer.close();
                        break;
                    case "close":
                        client.close();
                        break;
                    case "exitServer":
                        System.exit(0);
                        break;
                    case "generateJsonTabarnak":
                        System.out.println("["+TimeManager.getTime()+"] Generating content.json");
                        ContentGenerator.generate();
                        System.out.println("["+TimeManager.getTime()+"] content.json file generated !");
                        break;
                    default:
                        writer.write("null");
                        writer.flush();
                        break;

                }
                client.close();
            } catch (SocketException e){
                e.printStackTrace();
                try {
                    client.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readContent(){
        try {
            FileReader fileReader = new FileReader(new File(System.getProperty("user.dir")+"/content.json"));
            int i = fileReader.read();
            StringBuilder content = new StringBuilder();
            while(i != -1){
                content.append((char) i);
                i = fileReader.read();
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String read() throws IOException{
        InputStream  socketInputStream = client.getInputStream();
        int expectedDataLength = 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(expectedDataLength);
        byte[] chunk = new byte[expectedDataLength];
        int numBytesJustRead;
        while((numBytesJustRead = socketInputStream.read(chunk)) != -1) {
            baos.write(chunk, 0, numBytesJustRead);
        }
        String msg =  baos.toString("UTF-8");
        System.out.println(msg);
        return msg;
    }
}
