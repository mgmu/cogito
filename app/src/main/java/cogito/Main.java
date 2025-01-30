package cogito;

import javax.swing.SwingUtilities;
import cogito.view.MainScreen;
import cogito.view.FrameManager;

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
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}
