package fr.modcraftmc.launcher.core.exceptions;

public class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(String error) {
        super(error);
    }
}
