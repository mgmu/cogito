package cogito.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainPanel extends JPanel {

    // Preferred width of the panel
    private static final int PREFERRED_WIDTH = 400;

    // Preferred height of the panel
    private static final int PREFERRED_HEIGHT = 200;
    
    public MainPanel() {
        // Creates a new project
        JButton newButton = createNamedButton(
          "New",
          KeyEvent.VK_N,
          ae -> {
              throw new UnsupportedOperationException("todo");
          }
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
