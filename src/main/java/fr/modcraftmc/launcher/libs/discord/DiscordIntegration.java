package fr.modcraftmc.launcher.libs.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.modcraftmc.launcher.core.ModcraftLauncher;

public class DiscordIntegration {

    private Thread discord;

    public DiscordIntegration() {

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> ModcraftLauncher.LOGGER.info("Connected to user : " + user.username + user.discriminator);
        lib.Discord_Initialize("637707031804903425", handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "modcraftmc.fr";
        lib.Discord_UpdatePresence(presence);
        discord = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler");

    }

    public void setState(boolean state) {
        if (state) {
            discord.start();
        } else {
            discord.interrupt();
        }
    }

}
