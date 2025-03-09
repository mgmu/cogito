package cogito.view;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import cogito.model.Graph;
import cogito.model.Node;

/**
 * The first screen of the GUI.
 *
 * This Screen prompts the user to choose between creating a new graph or
 * loading an existing one.
 */
public class MainScreen extends Screen {

    // Preferred width of the panel
    private static final int PREFERRED_WIDTH = 400;

    // Preferred height of the panel
    private static final int PREFERRED_HEIGHT = 200;
    
    /**
     * Creates a new main screen with the given frame manager.
     *
     * @param frameManager The frame manager of the application.
     */
    public MainScreen(FrameManager frameManager) {
        super(frameManager);

        // Creates a new project
        JButton newButton = createNamedButton(
          "New",
          KeyEvent.VK_N,
          ae -> this.frameManager.setCurrentScreen(
            new GraphEditor(this.frameManager, new Graph())
          )
        );

        // Loads an existing project
        JButton loadButton = createNamedButton(
          "Load",
          KeyEvent.VK_L,
          ae -> {
              throw new UnsupportedOperationException("todo");
          }
        );

        this.add(newButton);
        this.add(loadButton);
    }

    // Returns a named button, sets its mnemonic and action listener
    private JButton createNamedButton(String name, int mnemonic,
            ActionListener al) {
        JButton button = new JButton(name);
        button.setMnemonic(mnemonic);
        button.addActionListener(al);
        return button;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
