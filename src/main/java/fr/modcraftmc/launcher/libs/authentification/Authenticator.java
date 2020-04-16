package fr.modcraftmc.launcher.libs.authentification;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.modcraftmc.launcher.libs.authentification.exception.AuthentificationException;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;

public class Authenticator {
    public static AuthInfos authInfos;
    public static boolean isLogged = false;

    public static void auth(String email, String password) throws AuthentificationException {

        fr.litarvan.openauth.Authenticator authenticator = new fr.litarvan.openauth.Authenticator(fr.litarvan.openauth.Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

        AuthResponse response = null;
        try {
            response = authenticator.authenticate(AuthAgent.MINECRAFT, email, password, "");
        } catch (AuthenticationException e) {
            throw new AuthentificationException("Erreur avec mojang", 3);
        }
        authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
        isLogged = true;

    }

    public static void auth(String accesToken) throws AuthentificationException {

    }

}
