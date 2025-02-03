package cogito.view;

import java.awt.geom.Ellipse2D;
import java.util.Objects;
import cogito.model.Node;

public class NodeView extends Ellipse2D.Double implements Observer {

    // The model that this NodeView represents
    private Node model;

    // Error message to show when a null object is given
    private static final String NULL_OBJECT_ERROR = "Object can not be null";

    // Error message to show when a given object is not a node
    private static final String NOT_A_NODE_ERROR =
        "Object must be an instance of Node";

    // Error message to show when a node is null
    private static final String NULL_NODE_ERROR = "Node can not be null";

    /**
     * Creates a new Node view that updates according to changes on the
     * specified model.
     *
     * @param model The model that this NodeView represents, not null.
     */
    public NodeView(Node model) {
        this.model = Objects.requireNonNull(model, NULL_NODE_ERROR);
        // model.subscribe(this); // wait for end of draw
    }
    
    @Override
    public void updateWithData(Object object) {
        Objects.requireNonNull(object, NULL_OBJECT_ERROR);
        if (!(object instanceof Node))
            throw new IllegalArgumentException(NOT_A_NODE_ERROR);
        this.model = (Node)object;
        // does nothing with new data for the moment
    }
}
