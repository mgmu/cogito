package cogito.view;

import java.util.Objects;
import java.awt.Dimension;
import java.awt.Color;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import cogito.controller.GraphEditorMouseController;
import cogito.controller.AddNodeController;
import cogito.controller.SelectNodeController;
import cogito.controller.LinkNodeController;
import cogito.controller.RemoveNodeController;
import cogito.model.Graph;
import cogito.util.DataManager;

/**
 * Contains the buttons that edit the graph.
 */
public class EditButtonsBar extends JPanel {
    
    // Preferred dimensions of this bar.
    private final int preferredWidth;
    private final int preferredHeight;

    // The mouse controller of the graph editor.
    private GraphEditorMouseController currentController;

    // The view of the edited graph.
    private GraphView graphView;

    // The graph model represented in the graph editor.
    private Graph graphModel;

    // The detailed node view
    private DetailedNodeView detailedNodeView;

    private FrameManager frameManager;

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
      int height,
      FrameManager frameManager
    ) {
        this.graphView = Objects.requireNonNull(graphView,
                NULL_GRAPH_VIEW_ERROR);
        this.detailedNodeView = Objects.requireNonNull(detailedNodeView,
                "Detailed node view can not be null");
        this.graphModel = Objects.requireNonNull(graphModel, NULL_GRAPH_ERROR);
        this.preferredWidth = width;
        this.preferredHeight = height;
        this.currentController = new AddNodeController(
          this.graphView,
          this.graphModel,
          this.detailedNodeView
        );
        this.currentController.enable();
        this.frameManager = frameManager;

        // Add node button
        JButton addNodeButton = new JButton("Add node");
        addNodeButton.addActionListener(
          al -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof AddNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new AddNodeController(
                this.graphView,
                this.graphModel,
                this.detailedNodeView
              );
              this.currentController.enable();
          }
        );
        this.add(addNodeButton);

        // Select node button
        JButton selectNodeButton = new JButton("Select/move node");
        selectNodeButton.addActionListener(
          al -> {
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
          al -> {
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

        // Remove node button
        JButton removeNodeButton = new JButton("Remove node");
        removeNodeButton.addActionListener(
          al -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof RemoveNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new RemoveNodeController(
                this.graphView,
                this.graphModel,
                this.detailedNodeView
              );
              this.currentController.enable();
          }
        );
        this.add(removeNodeButton);

        // Save graph button
        JButton saveGraphButton = new JButton("Save graph");
        saveGraphButton.addActionListener(
          al -> this.saveGraph()
        );
        this.add(saveGraphButton);

        JButton backToMainScreenButton = new JButton("Back to main screen");
        backToMainScreenButton.addActionListener(
          al -> {
              this.saveGraph();
              this.frameManager.setCurrentScreen(
                new MainScreen(this.frameManager)
              );
          }
        );
        this.add(backToMainScreenButton);
        
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }

    private void saveGraph() {
        try {
            DataManager.saveGraph(this.graphModel);
            JOptionPane.showMessageDialog(
              this.graphView.getAppFrame(),
              "Graph succesfully saved!",
              "Succes",
              JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
              this.graphView.getAppFrame(),
              "I/O error: " + ioe.getMessage(),
              "Graph could not be saved",
              JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
