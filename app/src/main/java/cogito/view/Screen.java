package cogito.view;

import java.util.Objects;
import javax.swing.JPanel;

/**
 * A Screen is a JPanel with the ability to change the screen displayed on the
 * frame of the application.
 *
 * Every panel that wants this ability should be a Screen.
 */
public class Screen extends JPanel {
    
    /**
     * The frame manager of the application.
     */
    protected FrameManager frameManager;

    /**
     * Creates a new Screen with the specified frame manager.
     *
     * @param frameManager The frame manager of the application, can be null.
     */
    public Screen(FrameManager frameManager) {
        this.frameManager = frameManager;
    }

    /**
     * Sets the frame manager of this Screen.
     *
     * @param frameManager The new frame manager of this Screen, not null.
     * @throws NullPointerException if frameManager is null.
     */
    public void setFrameManager(FrameManager frameManager) {
        Objects.requireNonNull(frameManager);
        this.frameManager = frameManager;
    }
}
