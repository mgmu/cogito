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
import cogito.model.Node;

/**
 * Detailed view of a selected node.
 */
public class DetailedNodeView extends JPanel implements Observer {

    // The node model.
    private Node model;

    // Preferred Dimension of this DetailedNodeView.
    private int preferredWidth;
    private int preferredHeight;

    private static final String NO_INFO = "No information";

    private JTextField title;
    private JTextArea information;
    private JButton editButton;
    private JButton saveButton;
    
    /**
     * Creates a new detailed view of a node with the specified dimensions.
     *
     * @param width The preferred width of this DetailedNodeView.
     * @param height The preferred height of this DetailedNodeView.
     */
    public DetailedNodeView(int width, int height) {
        this.model = null;
        this.preferredWidth = width;
        this.preferredHeight = height;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.title = new JTextField(NO_INFO, 10);
        this.title.setEditable(false);
        this.information = new JTextArea();
        this.information.setEditable(false);
        this.information.setLineWrap(true);

        // Edit button
        this.editButton = new JButton("Edit");
        this.editButton.setEnabled(false);
        this.editButton.addActionListener(
          al -> {
              this.title.setEditable(true);
              this.information.setEditable(true);
              this.saveButton.setEnabled(true);
          }
        );

        // Save button
        this.saveButton = new JButton("Save");
        this.saveButton.setEnabled(false);
        this.saveButton.addActionListener(
          al -> {
              this.model.setTitle(this.title.getText());
              this.model.setInformation(this.information.getText());
              this.title.setEditable(false);
              this.information.setEditable(false);
              this.saveButton.setEnabled(false);
              this.model.updateObservers();
          }
        );

        // Title panel
        JPanel titlePane = new JPanel();
        titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.LINE_AXIS));
        titlePane.add(new JLabel("Title:"));
        titlePane.add(this.title);

        // Information panel
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.PAGE_AXIS));
        infoPane.add(new JLabel("Information:"));
        JScrollPane scrollPane = new JScrollPane(this.information);
        infoPane.add(scrollPane);
        infoPane.add(editButton);
        infoPane.add(saveButton);

        // Add all components
        this.add(titlePane);
        this.add(infoPane);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        this.title.setText(model.getTitle());
        this.information.setText(model.getInformation());
        this.model.subscribe(this);
        this.editButton.setEnabled(true);
        this.repaint();
    }

    @Override
    public void updateWithData(Object object) {
        Node node = (Node)object;
        this.title.setText(node.getTitle());
        this.information.setText(node.getInformation());
        this.repaint();
    }
}
