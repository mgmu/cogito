package cogito;

import javax.swing.SwingUtilities;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import cogito.view.MainScreen;
import cogito.view.FrameManager;
import cogito.util.DataManager;

/**
 * Entry class of the program.
 */
public class Main {

    private static void createAndShowGui() {
        MainScreen mainScreen = new MainScreen(null);
        FrameManager frameManager = new FrameManager(mainScreen);
        frameManager.showGui();
    }

    /**
     * Entry point of the program.
     *
     * @param args The arguments given to the program.
     */
    public static void main(String[] args) {
        try {
            Path instalDir = DataManager.getInstalDir();
            if (Files.notExists(instalDir))
                Files.createDirectory(instalDir);
            Path graphDir = DataManager.getGraphsDir();
            if (Files.notExists(graphDir))
                Files.createDirectory(graphDir);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}
