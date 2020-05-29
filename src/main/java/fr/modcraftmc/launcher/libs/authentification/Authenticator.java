package fr.modcraftmc.launcher.libs.authentification;

import com.azuriom.azauth.AzAuthenticator;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.litarvan.openauth.model.response.RefreshResponse;
import fr.modcraftmc.launcher.libs.authentification.exception.AuthentificationException;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;

import java.io.IOException;

public class Authenticator {
    public static AuthInfos authInfos;
    public static boolean isLogged = false;
    private static final fr.litarvan.openauth.Authenticator authenticator = new fr.litarvan.openauth.Authenticator(fr.litarvan.openauth.Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

    public static void auth(String email, String password) throws AuthentificationException {

        AuthResponse response = null;
        try {
            response = authenticator.authenticate(AuthAgent.MINECRAFT, email, password, "");
            authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
            isLogged = true;
        } catch (AuthenticationException e) {

            try {
                AzAuthenticator authenticator = new AzAuthenticator("https://modcraftmc.fr");
                authInfos = authenticator.authenticate(email, password, AuthInfos.class);
            } catch (IOException | com.azuriom.azauth.AuthenticationException ioException) {
                isLogged = false;
                ioException.printStackTrace();
                throw new AuthentificationException("Erreur de connexion", 5);
            }

        }

    }

    public static void refresh(String accesToken, String clientToken) throws AuthentificationException {

        try {
            RefreshResponse refresh = authenticator.refresh(accesToken, clientToken);
            authInfos = new AuthInfos(refresh.getSelectedProfile().getName(), refresh.getAccessToken(), refresh.getSelectedProfile().getId());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new AuthentificationException("", 3);
        }

    }
}
