package cogito.view;

import java.util.Objects;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
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
import cogito.controller.UnlinkNodeController;
import cogito.model.Graph;
import cogito.util.DataManager;

/**
 * Groups the control buttons of the graph view.
 */
public class EditButtonsBar extends JPanel {
    
    /**
     * Preferred width of this bar.
     */
    private final int preferredWidth;

    /**
     * Preferred height of this bar.
     */
    private final int preferredHeight;

    /**
     * The mouse controller of the graph editor.
     */
    private GraphEditorMouseController currentController;

    /**
     * The view of the edited graph.
     */
    private GraphView graphView;

    /**
     * The graph model represented in the graph editor.
     */
    private Graph graphModel;

    /**
     * The detailed node view.
     */
    private DetailedNodeView detailedNodeView;

    /**
     * The frame manager.
     */
    private FrameManager frameManager;

    private JPanel graphButtonsPane;
    private JPanel generalButtonsPane;

    // Error messages
    private static final String NULL_GRAPH_VIEW_ERROR =
        "GraphView can not be null";
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    /**
     * Creates a new bar populated with control buttons.
     *
     * @param graphView The view of the graph model, not null.
     * @param detailedNodeView The detailed node view of the graph editor.
     * @param graphModel The graph model, not null.
     * @param width The preferred width of the bar.
     * @param height The preferred height of the bar.
     * @param frameManager The frame manager of the app.
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
        this.graphButtonsPane = EditButtonsBar.createButtonsSubPane("Graph");
        this.generalButtonsPane = EditButtonsBar.createButtonsSubPane(
          "General"
        );

        this.currentController = new AddNodeController(
          this.graphView,
          this.graphModel,
          this.detailedNodeView
        );
        this.currentController.enable();
        this.frameManager = frameManager;

        // Layout
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        this.setLayout(layout);
        this.setBorder(BorderFactory.createLoweredBevelBorder());

        // Select node button
        JButton selectNodeButton = new JButton("Select/move");
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
        this.graphButtonsPane.add(selectNodeButton);

        // Add node button
        JButton addNodeButton = new JButton("Add");
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
        this.graphButtonsPane.add(addNodeButton);

        // Remove node button
        JButton removeNodeButton = new JButton("Remove");
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
        this.graphButtonsPane.add(removeNodeButton);

        // Link node button
        JButton linkNodeButton = new JButton("Link");
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
        this.graphButtonsPane.add(linkNodeButton);

        // Unlink nodes button
        JButton unlinkNodeButton = new JButton("Unlink");
        unlinkNodeButton.addActionListener(
          al -> {
              if (this.currentController != null) {
                  if (this.currentController instanceof UnlinkNodeController)
                      return;
                  this.currentController.disable();
              }
              this.currentController = new UnlinkNodeController(
                this.graphView,
                this.graphModel
              );
              this.currentController.enable();
          }
        );
        this.graphButtonsPane.add(unlinkNodeButton);

        // Save graph button
        JButton saveGraphButton = new JButton("Save");
        saveGraphButton.addActionListener(
          al -> this.saveGraph()
        );
        this.generalButtonsPane.add(saveGraphButton);

        // Back to main screen button
        JButton backToMainScreenButton = new JButton("Back to main screen");
        backToMainScreenButton.addActionListener(
          al -> {
              this.saveGraph();
              this.frameManager.setCurrentScreen(
                new MainScreen(this.frameManager)
              );
          }
        );
        this.generalButtonsPane.add(backToMainScreenButton);

        this.add(this.graphButtonsPane);
        this.add(this.generalButtonsPane);
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

    // creates a jpanel with a leading flow layout and a default titled border
    // around it with the provided title
    private static JPanel createButtonsSubPane(String title) {
        JPanel pane = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        pane.setLayout(layout);
        pane.setBorder(BorderFactory.createTitledBorder(title));
        return pane;
    }
}
