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
    private static final int PREFERRED_WIDTH = 800;
    private static final int PREFERRED_HEIGHT = 600;

    // The model to edit
    private final Graph model;

    // Error message to show when graph is null
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    public GraphEditor(FrameManager frameManager, Graph model) {
        super(frameManager);
        Objects.requireNonNull(model, NULL_GRAPH_ERROR);
        this.model = model;

        this.setLayout(new BorderLayout());

        this.add(new EditButtonsBar(), BorderLayout.NORTH);
        this.add(new GraphView(), BorderLayout.WEST);
        this.add(new DetailedNodeView(), BorderLayout.EAST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
