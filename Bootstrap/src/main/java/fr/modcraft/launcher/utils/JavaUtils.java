package fr.modcraft.launcher.utils;

public class JavaUtils {

    public static String getArchitecture() {
        return System.getProperty("sun.arch.data.model");
    }

    public static String getVersion() {
        return System.getProperty("java.version");
    }

    public static boolean is64Bits() {
        return JavaUtils.getArchitecture().contains("64");
    }

}
