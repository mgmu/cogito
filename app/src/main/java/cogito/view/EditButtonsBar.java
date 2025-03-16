package cogito.view;

import java.util.Objects;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import cogito.controller.GraphEditorMouseController;
import cogito.controller.AddNodeController;
import cogito.controller.SelectNodeController;
import cogito.controller.LinkNodeController;
import cogito.model.Graph;

/**
 * Contains the buttons that edit the graph.
 */
public class EditButtonsBar extends JPanel {
    
    // Preferred dimensions of this bar.
    private final int preferredWidth;
    private final int preferredHeight;

    // The mouse controller of the graph editor.
    private GraphEditorMouseController currentController = null;

    // The view of the edited graph.
    private GraphView graphView;

    // The graph model represented in the graph editor.
    private Graph graphModel;

    // The detailed node view
    private DetailedNodeView detailedNodeView;

    // Error messages
    private static final String NULL_GRAPH_VIEW_ERROR =
        "GraphView can not be null";
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    /**
     * Creates a new buttons bar populated with supported edit operations.
     *
     * @param graphView The view of the graph model, not null.
     * @param graphModel The graph model, not null.
     * @param width The preferred width of the bar.
     * @param height The preferred height of the bar.
     */
    public EditButtonsBar(
      GraphView graphView,
      DetailedNodeView detailedNodeView,
      Graph graphModel,
      int width,
      int height
    ) {
        this.graphView = Objects.requireNonNull(graphView,
                NULL_GRAPH_VIEW_ERROR);
        this.detailedNodeView = Objects.requireNonNull(detailedNodeView,
                "Detailed node view can not be null");
        this.graphModel = Objects.requireNonNull(graphModel, NULL_GRAPH_ERROR);
        this.preferredWidth = width;
        this.preferredHeight = height;

        // Add node button
        JButton addNodeButton = new JButton("Add node");
        addNodeButton.addActionListener(
          ae -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof AddNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new AddNodeController(
                this.graphView,
                this.graphModel
              );
              this.currentController.enable();
          }
        );
        this.add(addNodeButton);

        // Select node button
        JButton selectNodeButton = new JButton("Select node");
        selectNodeButton.addActionListener(
          ae -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof SelectNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new SelectNodeController(
                this.graphView,
                this.detailedNodeView,
                this.graphModel
              );
              this.currentController.enable();
          }
        );
        this.add(selectNodeButton);

        // Link node button
        JButton linkNodeButton = new JButton("Link node");
        linkNodeButton.addActionListener(
          ae -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof LinkNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new LinkNodeController(
                this.graphView,
                this.graphModel
              );
              this.currentController.enable();
          }
        );
        this.add(linkNodeButton);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }
}
