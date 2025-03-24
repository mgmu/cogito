package cogito.view;

import java.util.Objects;
import javax.swing.JFrame;

/**
 * Manages the JFrame of the application.
 *
 * A reference to this object should be passed to each Screen in order to be
 * able to switch between screens of the application.
 */
public class FrameManager {
    
    // The frame of the application.
    private final JFrame frame;

    // The current screen in the content pane of the embedded frame.
    private Screen currentScreen;

    // Error message to show when a screen is null.
    private static final String NULL_SCREEN_ERROR = "Screen must not be null";

    /**
     * Creates a new FrameManager without a screen.
     */
    public FrameManager() {
        this.frame = new JFrame("Cogito");
        this.currentScreen = null;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets the frame of the application to visible.
     *
     * This method should only be run in the GUI thread. Otherwise, behavior is
     * undefined.
     */
    public void showGui() {
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    /**
     * Sets the current screen.
     *
     * @param screen The new current screen, not null.
     * @throws NullPointerException if screen is null.
     */
    public void setCurrentScreen(Screen screen) {
        Objects.requireNonNull(screen, NULL_SCREEN_ERROR);
        if (this.currentScreen != null)
            this.frame.getContentPane().remove(this.currentScreen);
        this.currentScreen = screen;
        this.frame.getContentPane().add(this.currentScreen);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);

        // mysterious, ensure usefulness
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Returns the frame of the application.
     *
     * @return The application JFrame.
     */
    public JFrame getAppFrame() {
        return this.frame;
    }
}
