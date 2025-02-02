package cogito.view;

import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * View of the graph model.
 */
public class GraphView extends JPanel {

    public GraphView() {
        JLabel label = new JLabel("Graph view");

        this.add(label);
    }
}
