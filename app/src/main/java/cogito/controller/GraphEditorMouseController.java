package cogito.controller;

import java.util.Objects;
import java.awt.event.MouseAdapter;
import cogito.model.Graph;
import cogito.view.GraphView;

/**
 * Base class of the various controllers of the graph editor.
 */
public abstract class GraphEditorMouseController extends MouseAdapter {

    /**
     * The view of the edited graph.
     */
    protected GraphView view;

    /**
     * The graph model represented in the graph editor.
     */
    protected Graph model;

    // Error messages
    private static final String NULL_GRAPH_VIEW_ERROR =
        "GraphView can not be null";
    private static final String NULL_GRAPH_ERROR = "Graph can not be null";

    /**
     * Creates a new graph editor mouse controller on the specified view and
     * model.
     *
     * @param view The GraphView this controller listens to, not null.
     * @param model The Graph this controller acts on, not null.
     */
    public GraphEditorMouseController(GraphView view, Graph model) {
        this.view = Objects.requireNonNull(view, NULL_GRAPH_VIEW_ERROR);
        this.model = Objects.requireNonNull(model, NULL_GRAPH_ERROR);
    }

    /**
     * Enables this controller by adding it to the mouse listeners and mouse
     * motion listeners of this contoller's GraphView.
     */
    public void enable() {
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    /**
     * Disables this controller by removing it from the mouse listeners and
     * mouse motion listeners of this contoller's GraphView.
     */
    public void disable() {
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
    }
}
