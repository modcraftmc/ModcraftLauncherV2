package fr.modcraftmc.launcher.libs.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.ui.ModcraftApplication;

public class DiscordIntegration {

    private final Thread discord;
    private static final DiscordRPC lib = DiscordRPC.INSTANCE;
    private static final long start = System.currentTimeMillis() / 1000;


    public DiscordIntegration() {


        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> ModcraftLauncher.LOGGER.info("Connected to user : " + user.username + user.discriminator);
        lib.Discord_Initialize("637707031804903425", handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = start;
        presence.state = ModcraftApplication.statusDiscord;
        presence.details = "joue sur ModcraftMC";
        presence.largeImageKey = "logo";
        lib.Discord_UpdatePresence(presence);
        discord = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler");
        discord.start();

    }


    public static void updateState(String state) {
        ModcraftApplication.statusDiscord = state;
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.state = ModcraftApplication.statusDiscord;
        presence.startTimestamp = start;
        presence.details = "joue sur ModcraftMC";
        presence.largeImageKey = "logo";
        lib.Discord_UpdatePresence(presence);
    }

}
