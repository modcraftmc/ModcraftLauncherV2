package fr.modcraftmc.launcher.libs.settings.impl;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.modcraftmc.launcher.core.ModcraftLauncher;
import fr.modcraftmc.launcher.libs.settings.ISetting;

public class DiscordIntegration extends ISetting {

    public static Thread rpcThread;

    public DiscordIntegration() {

        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "637707031804903425";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> ModcraftLauncher.LOGGER.info("discord connected to {}", user.username);
        lib.Discord_Initialize(applicationId, handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Skyblock moddé 1.15.2 !";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        rpcThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        });rpcThread.interrupt();
    }

    public Thread getRpcThread() {
        return rpcThread;
    }

    @Override
    public String getName() {
        return "DiscordRPC";
    }

    @Override
    public String getDescription() {
        return "Discord rich presence";
    }


}
