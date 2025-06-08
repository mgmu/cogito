package cogito.view;

import java.util.Objects;
import java.util.List;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import cogito.model.Node;

/**
 * Detailed view of a selected node.
 */
public class DetailedNodeView extends JPanel {

    // The node model.
    private Node model;

    // Preferred width of this detailed node view.
    private int preferredWidth;

    // Preferred height of this detailed node view.
    private int preferredHeight;

    private static final String NO_INFO = "No information";

    // The node title field.
    private JTextField titleField;

    // The node information text area.
    private JTextArea infoArea;

    // The save button.
    private JButton saveButton;

    // The application frame.
    private JFrame appFrame;
    
    // Node title and information documents listeners
    private NodeInformationListener nodeInformationListener;
    private NodeTitleListener nodeTitleListener;

    /**
     * Creates a new detailed view of a node with the specified dimensions.
     *
     * @param width The preferred width of this DetailedNodeView.
     * @param height The preferred height of this DetailedNodeView.
     * @param appFrame The application frame.
     */
    public DetailedNodeView(int width, int height, JFrame appFrame) {
        this.model = null;
        this.preferredWidth = width;
        this.preferredHeight = height;
        this.appFrame = appFrame;
        this.nodeInformationListener = null;
        this.nodeTitleListener = null;

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        // Title label
        JLabel titleLabel = new JLabel("Title:");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(titleLabel, constraints);

        // Title textfield
        this.titleField = new JTextField(NO_INFO);
        this.titleField.setEditable(false);
        constraints.weightx = 1.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(this.titleField, constraints);

        // Information label
        JLabel infoLabel = new JLabel("Information:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(infoLabel, constraints);

        // Information area
        this.infoArea = new JTextArea(NO_INFO);
        this.infoArea.setEditable(false);
        this.infoArea.setLineWrap(true);
        this.infoArea.setWrapStyleWord(true);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(this.infoArea, constraints);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }

    /**
     * Sets the model and saves viewed information.
     *
     * @param model The new model of this view.
     */
    public void setModel(Node model) {
        this.model = model;

        // Setup title document listener
        this.titleField.setText(model.getTitle());
        this.nodeTitleListener = new NodeTitleListener(
          this.appFrame,
          this.model
        );
        this.titleField
            .getDocument()
            .addDocumentListener(this.nodeTitleListener);
        this.titleField.setEditable(true);

        // Setup information document listener
        this.infoArea.setText(model.getInformation());
        this.nodeInformationListener = new NodeInformationListener(
          this.appFrame,
          this.model
        );
        this.infoArea
            .getDocument()
            .addDocumentListener(this.nodeInformationListener);
        this.infoArea.setEditable(true);

        this.repaint();
    }

    /**
     * Returns the current model of this detailed node view.
     *
     * @return The node model, or null if there is none.
     */
    public Node getModel() {
        return this.model;
    }

    /**
     * Clears the model of this detailed node view.
     */
    public void clearModel() {
        this.model = null;

        // remove title document listener before clearing title field
        this.titleField
            .getDocument()
            .removeDocumentListener(this.nodeTitleListener);
        this.titleField.setText(NO_INFO);
        this.titleField.setEditable(false);

        // remove information document listener before clearing title field
        this.infoArea
            .getDocument()
            .removeDocumentListener(this.nodeInformationListener);
        this.infoArea.setText(NO_INFO);
        this.infoArea.setEditable(false);

        this.repaint();
    }
}
