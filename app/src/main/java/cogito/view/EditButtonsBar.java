package cogito.view;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Contains the buttons that edit the graph.
 */
public class EditButtonsBar extends JPanel {
    
    /**
     * Creates a new buttons bar populated with supported edit operations.
     */
    public EditButtonsBar() {
        JButton doNothing = new JButton("Do nothing");

        this.add(doNothing);
    }
}
