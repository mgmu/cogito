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
          PREFERRED_WIDTH - 400,
          PREFERRED_HEIGHT - 50,
          frameManager.getAppFrame()
        );
        this.detailedNodeView = new DetailedNodeView(
          400,
          PREFERRED_HEIGHT - 50,
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
            50,
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
