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
public class DetailedNodeView extends JPanel implements Observer {

    /**
     * The node model.
     */
    private Node model;

    /**
     * Preferred width of this detailed node view.
     */
    private int preferredWidth;

    /**
     * Preferred height of this detailed node view.
     */
    private int preferredHeight;

    private static final String NO_INFO = "No information";

    /**
     * The node title field.
     */
    private JTextField titleField;

    /**
     * The node information text area.
     */
    private JTextArea infoArea;

    /**
     * The update button.
     */
    private JButton updateButton;

    /**
     * The application frame.
     */
    private JFrame appFrame;
    
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

        // Update button
        this.updateButton = new JButton("Update");
        this.updateButton.setEnabled(false);
        this.updateButton.addActionListener(
          al -> this.updateModel()
        );

        // Buttons pane
        JPanel buttonsPane = new JPanel();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 1;
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.LAST_LINE_END;
        buttonsPane.add(this.updateButton);
        this.add(buttonsPane, constraints);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }

    /**
     * Sets the model and updates viewed information.
     *
     * Unsubscribes from the previous model if there was any.
     *
     * @param model The new model of this view.
     */
    public void setModel(Node model) {
        if (this.model != null)
            this.model.unsubscribe(this);
        this.model = model;
        this.titleField.setText(model.getTitle());
        this.infoArea.setText(model.getInformation());
        this.model.subscribe(this);
        this.titleField.setEditable(true);
        this.infoArea.setEditable(true);
        this.updateButton.setEnabled(true);
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

    @Override
    public void updateWithData(Object object) {
        Node node = (Node)object;
        this.titleField.setText(node.getTitle());
        this.infoArea.setText(node.getInformation());
        this.repaint();
    }

    /**
     * Clears the model of this detailed node view.
     *
     * This detailed node view unsubscribes from it before removal.
     */
    public void clearModel() {
        if (this.model != null)
            this.model.unsubscribe(this);
        this.model = null;
        this.titleField.setText(NO_INFO);
        this.titleField.setEditable(false);
        this.infoArea.setText(NO_INFO);
        this.infoArea.setEditable(false);
        this.updateButton.setEnabled(false);
        this.repaint();
    }

    // Updates model title if it changed, if model is not null.
    // Returns true if the operation was succesfull.
    private boolean updateModelTitle() {
        if (this.model == null)
            return true;
        String newTitle = this.titleField.getText();
        if (!newTitle.equals(this.model.getTitle())) {
            try {
                this.model.setTitle(newTitle);
                this.model.updateObservers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                  this.appFrame,
                  e.getMessage(),
                  "Node title could not be updated",
                  JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        return true;
    }

    // Updates model information if it changed, if model is not null.
    // Returns true if the operation was succesfull.    
    private boolean updateModelInformation() {
        if (this.model == null)
            return true;
        String newInfo = this.infoArea.getText();
        if (!newInfo.equals(this.model.getInformation())) {
            try {
                this.model.setInformation(this.infoArea.getText());
                this.model.updateObservers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                  this.appFrame,
                  e.getMessage(),
                  "Node information could not be updated",
                  JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        return true;
    }

    /**
     * Updates model title and information.
     *
     * @return true if the operation was succesfull.
     */
    public boolean updateModel() {
        return this.updateModelTitle() && this.updateModelInformation();
    }
}
