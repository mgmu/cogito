package cogito.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

/**
 * Detailed view of a selected node.
 */
public class DetailedNodeView extends JPanel {

    public DetailedNodeView() {
        JLabel title = new JLabel("title of the node");
        this.add(title);

        this.add(new JScrollPane());
    }
}
