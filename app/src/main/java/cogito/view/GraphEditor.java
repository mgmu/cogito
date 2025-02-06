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
    private static final int PREFERRED_WIDTH = 1920;
    private static final int PREFERRED_HEIGHT = 1080;

    // The model to edit
    private final Graph model;

    // The view of the model to edit.
    private final GraphView graphView;

    // Error message to show when graph is null
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    public GraphEditor(FrameManager frameManager, Graph model) {
        super(frameManager);
        Objects.requireNonNull(model, NULL_GRAPH_ERROR);
        this.model = model;
        this.graphView = new GraphView(this.model, PREFERRED_WIDTH - 400,
                PREFERRED_HEIGHT - 50);
        this.model.subscribe(this.graphView);

        this.setLayout(new BorderLayout());

        this.add(
          new EditButtonsBar(this.graphView, this.model, PREFERRED_WIDTH, 50),
          BorderLayout.NORTH
        );
        this.add(this.graphView, BorderLayout.CENTER);
        this.add(new DetailedNodeView(400, PREFERRED_HEIGHT - 50),
                BorderLayout.EAST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
