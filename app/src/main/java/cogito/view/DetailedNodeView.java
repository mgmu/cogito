package cogito.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Dimension;

/**
 * Detailed view of a selected node.
 */
public class DetailedNodeView extends JPanel {

    // Preferred Dimension of this DetailedNodeView
    private int preferredWidth;
    private int preferredHeight;

    /**
     * Creates a new detailed view of a node with the specified dimensions.
     *
     * @param width The preferred width of this DetailedNodeView.
     * @param height The preferred height of this DetailedNodeView.
     */
    public DetailedNodeView(int width, int height) {
        this.preferredWidth = width;
        this.preferredHeight = height;

        JLabel title = new JLabel("title of the node");
        this.add(title);

        this.add(new JScrollPane());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }
}
