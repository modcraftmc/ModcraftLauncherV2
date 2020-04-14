package ma.forix.gmserver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


public class ServerThread extends Thread implements Runnable {

    private Socket client;
    private PrintWriter writer;
    private BufferedInputStream reader;
    private String prelauncherJson, bootstrapJson;
    private File bootstrap = new File("prelauncher/bootstrap.jar");
    private File launcher = new File("bootsrap/launcher.jar");

    public ServerThread(Socket client){
        this.client = client;

        generatePrelauncher();
        generateBootstrap();

    }

    public void generatePrelauncher() {

        JSONArray prelauncher = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("path", "prelauncher/");
        object.put("filename", "bootstrap.jar");
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            //Get the checksum
            String checksum = ContentGenerator.getFileChecksum(md5Digest, bootstrap);
            object.put("md5", checksum);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        object.put("size", bootstrap.length());
        prelauncher.add(object);
        prelauncherJson = prelauncher.toJSONString();

    }

    public void generateBootstrap() {

        JSONArray bootstrap = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("path", "bootstrap/");
        object.put("filename", "launcher.jar");
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            //Get the checksum
            String checksum = ContentGenerator.getFileChecksum(md5Digest, launcher);
            object.put("md5", checksum);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        object.put("size", launcher.length());
        bootstrap.add(object);
        bootstrapJson = bootstrap.toJSONString();

    }

    @Override
    public void run() {
        super.run();
        System.out.println("["+TimeManager.getTime()+"] ["+getName()+"] Connection established !");
        while (!client.isClosed()) {
            System.out.println();
            try {
                writer = new PrintWriter(client.getOutputStream());
                reader = new BufferedInputStream(client.getInputStream());
                String reponse = read();
                System.out.println("["+TimeManager.getTime()+"] ["+getName()+"] New input from client received: "+reponse);
                switch (reponse){
                    case "getContent":
                        writer.write(Objects.requireNonNull(readContent()));
                        writer.flush();
                        break;
                    case "getContent-PRELAUNCHER":
                        writer.write(Objects.requireNonNull(readContent()));
                        writer.flush();
                        break;
                    case "getContent-BOOTSTRAP":
                        writer.write(bootstrapJson);
                        writer.flush();
                        break;
                    case "getContent-LAUNCHER":
                        writer.write(prelauncherJson);
                        writer.flush();
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

    private String readContent(){
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
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
