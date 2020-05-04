package fr.modcraftmc.launcher.libs.serverpinger;

import fr.modcraftmc.launcher.libs.discord.DiscordIntegration;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerPingerThread implements Runnable {

    private static MinecraftPingOptions options = new MinecraftPingOptions().setHostname("v1.modcraftmc.fr").setPort(25565);

    private Timer timer = new Timer();



    @Override
    public void run() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    MinecraftPingReply response = new MinecraftPing().getPing(options);
                    DiscordIntegration.setState(response.getPlayers().getOnline(), response.getPlayers().getMax());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 6000);

    }
}
