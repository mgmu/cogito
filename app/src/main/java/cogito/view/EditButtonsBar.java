package cogito.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Dimension;

/**
 * Contains the buttons that edit the graph.
 */
public class EditButtonsBar extends JPanel {
    
    private final int preferredWidth;
    private final int preferredHeight;

    /**
     * Creates a new buttons bar populated with supported edit operations.
     *
     * @param width The preferred width of the bar.
     * @param height The preferred height of the bar.
     */
    public EditButtonsBar(int width, int height) {
        this.preferredWidth = width;
        this.preferredHeight = height;
        JButton doNothing = new JButton("Do nothing");

        this.add(doNothing);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }
}
