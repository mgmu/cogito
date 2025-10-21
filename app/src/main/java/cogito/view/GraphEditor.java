package cogito.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Objects;
import cogito.model.Graph;

/**
 * The view that lets the user edit a graph.
 */
public class GraphEditor extends Screen {

    // Preferred dimensions of a GraphEditor
    /**
     * Preferred width of a graph editor.
     */
    private static final int PREFERRED_WIDTH = 1920;

    /**
     * Preferred height of a graph editor.
     */
    private static final int PREFERRED_HEIGHT = 1080;

    private static final int PREFERRED_WIDTH_DETAILED_NODE_VIEW = 400;
    private static final int PREFERRED_HEIGHT_EDIT_BUTTONS_BAR = 70;

    /**
     * The model to edit.
     */
    private final Graph model;

    /**
     * The view of the model to edit.
     */
    private final GraphView graphView;

    /**
     * The detailed node view.
     */
    private final DetailedNodeView detailedNodeView;

    // Error message to show when graph is null
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    /**
     * Creates a new graph editor for given model.
     *
     * @param frameManager The frameManager of the app.
     * @param model The graph model to edit.
     */
    public GraphEditor(FrameManager frameManager, Graph model) {
        super(frameManager);
        Objects.requireNonNull(model, NULL_GRAPH_ERROR);
        this.model = model;
        this.graphView = new GraphView(
          this.model,
          PREFERRED_WIDTH - PREFERRED_WIDTH_DETAILED_NODE_VIEW,
          PREFERRED_HEIGHT - PREFERRED_HEIGHT_EDIT_BUTTONS_BAR,
          frameManager.getAppFrame()
        );
        this.detailedNodeView = new DetailedNodeView(
          PREFERRED_WIDTH_DETAILED_NODE_VIEW,
          PREFERRED_HEIGHT - PREFERRED_HEIGHT_EDIT_BUTTONS_BAR,
          frameManager.getAppFrame()
        );
        this.model.subscribe(this.graphView);

        this.setLayout(new BorderLayout());
        this.add(
          new EditButtonsBar(
            this.graphView,
            this.detailedNodeView,
            this.model,
            PREFERRED_WIDTH,
            PREFERRED_HEIGHT_EDIT_BUTTONS_BAR,
            this.frameManager
          ),
          BorderLayout.NORTH
        );
        this.add(this.graphView, BorderLayout.CENTER);
        this.add(this.detailedNodeView, BorderLayout.EAST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
