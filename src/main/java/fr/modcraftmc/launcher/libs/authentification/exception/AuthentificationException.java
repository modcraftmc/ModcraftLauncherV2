package fr.modcraftmc.launcher.libs.authentification.exception;

import fr.modcraftmc.crashreporter.CrashReporter;
import fr.modcraftmc.launcher.ui.ModcraftApplication;

public class AuthentificationException extends Throwable {

    public AuthentificationException(String string, int errorKey) {
        super(string);
        CrashReporter.catchException(this, ModcraftApplication.window, errorKey);
    }
}
