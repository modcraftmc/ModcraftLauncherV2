package fr.modcraftmc.launcher.libs.serverpinger;

import fr.modcraftmc.launcher.libs.discord.DiscordIntegration;
import fr.modcraftmc.launcher.ui.ModcraftApplication;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerPingerThread implements Runnable {

    private static final MinecraftPingOptions options = new MinecraftPingOptions().setHostname("v1.modcraftmc.fr").setPort(25565);

    private final Timer timer = new Timer();



    @Override
    public void run() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    MinecraftPingReply response = new MinecraftPing().getPing(options);
                    DiscordIntegration.setState(response.getPlayers().getOnline(), response.getPlayers().getMax());

                    if (ModcraftApplication.mainLoaded) ModcraftApplication.mainController.setPlayerlist(
                            response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + "joueurs");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 60000);

    }
}
