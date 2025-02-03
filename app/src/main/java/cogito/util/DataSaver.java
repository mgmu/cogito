package cogito.util;

import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * Encapsulates convenience methods for storing data of the application.
 */
public class DataSaver {
    
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String INSTAL_DIR_NAME = ".cogito";
    private static final String GRAPHS_DIR_NAME = "graphs";

    /**
     * Returns the path of the installation directory.
     *
     * @return A Path denoting the installation directory.
     */
    public static Path getInstalDir() {
        return Paths.get(HOME_DIR, INSTAL_DIR_NAME);
    }

    /**
     * Returns the path of the graphs directory.
     *
     * @return A Path denoting the graphs directory.
     */
    public static Path getGraphsDir() {
        return Paths.get(HOME_DIR, INSTAL_DIR_NAME, GRAPHS_DIR_NAME);
    }
}
