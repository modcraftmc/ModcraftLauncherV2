package fr.modcraftmc.launcher.core.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    private static Path libsPath;

    static Path findLibsPath() {
        if (libsPath == null) {
            final Path asm = findJarPathFor("org/objectweb/asm/Opcodes.class", "asm");
            // go up SIX parents to find the libs dir
            final Path libs = asm.getParent().getParent().getParent().getParent().getParent().getParent();
            libsPath = libs;
        }
        return libsPath;
    }

    public static Path findJarPathFor(final String className, final String jarName) {
        final URL resource = test.class.getClassLoader().getResource(className);
        return findJarPathFor(className, jarName, resource);
    }

    public static Path findJarPathFor(final String resourceName, final String jarName, final URL resource) {
        try {
            Path path;
            final URI uri = resource.toURI();
            if (uri.getRawSchemeSpecificPart().contains("!")) {
                path = Paths.get(new URI(uri.getRawSchemeSpecificPart().split("!")[0]));
            } else {
                path = Paths.get(new URI("file://"+uri.getRawSchemeSpecificPart().substring(0, uri.getRawSchemeSpecificPart().length()-resourceName.length())));
            }
            System.out.println("Found JAR {} at path {}" + jarName +  path.toString());
            return path;
        } catch (NullPointerException | URISyntaxException e) {
            throw new RuntimeException("Unable to locate "+resourceName+" - "+jarName, e);
        }
    }
}
