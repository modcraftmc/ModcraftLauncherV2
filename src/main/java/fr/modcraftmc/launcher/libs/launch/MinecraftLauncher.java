package fr.modcraftmc.launcher.libs.launch;

import fr.modcraftmc.launcher.libs.authentification.Authenticator;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;

import static java.lang.Thread.sleep;
public class MinecraftLauncher {

    public static final GameVersion VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
    public static final GameInfos INFOS = new GameInfos("modcraftmc/instances/skyblock", VERSION, new GameTweak[] {GameTweak.FORGE}, "aaa");


    public void launch() throws LaunchException {

        ExternalLaunchProfile profile = fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, Authenticator.authInfos);
        //profile.getVmArgs().addAll(Arrays.asList(OptionApp.getRamArguments()));
        profile.getArgs().add("-Dfml.readTimeout=60");
        ExternalLauncher launcher = new ExternalLauncher(profile);
        Process p = null;
        try {
            p = launcher.launch();
            sleep(5000);
           // Platform.runLater(() -> ModcraftLauncher.getWindow().hide());
            p.waitFor();
        } catch (InterruptedException | LaunchException e) {
            e.printStackTrace();
        }
    }



}
