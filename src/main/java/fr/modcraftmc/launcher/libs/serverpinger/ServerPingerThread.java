package fr.modcraftmc.launcher.libs.serverpinger;

import fr.modcraftmc.launcher.libs.discord.DiscordIntegration;
import fr.modcraftmc.launcher.ui.ModcraftApplication;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerPingerThread implements Runnable {

    private static final MinecraftPingOptions options = new MinecraftPingOptions().setHostname("v1.modcraftmc.fr").setPort(25565);

    private final Timer timer = new Timer();
    private MinecraftPingReply response;


    public static void main(String[] args) {


    }

    public MinecraftPingReply getResponse() {
        return response;
    }

    @Override
    public void run() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    response = new MinecraftPing().getPing(options);
                    DiscordIntegration.updatePlayer(response.getPlayers().getOnline(), response.getPlayers().getMax());
                    System.out.println(response);

                    ModcraftApplication.mainController.setPlayerlist(
                            response.getPlayers().getOnline() + " / " + response.getPlayers().getMax() + " joueurs");


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 100, 60000);

    }


}
