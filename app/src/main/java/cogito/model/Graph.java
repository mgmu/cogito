package cogito.model;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Set;
import cogito.view.Observer;

/**
 * Encapsulates relations between Nodes in a graph space.
 *
 * A graph space is a XY coordinate system, where values of x increase from left
 * to right and values of y increase from top to bottom (note the difference
 * with traditional XY coordinate systems).
 */
public class Graph implements Observable {

    // Adjacency list of nodes
    private final Map<Node, ArrayList<Node>> adj;

    // The universally unique identifier of this Graph
    private final UUID identifier;

    // The observers subscribed to the updates of this Graph
    private final List<Observer> observers;

    // Error messages
    private static final String NULL_NODE_ERROR = "Node must not be null";
    private static final String NODE_ALREADY_IN_GRAPH_ERROR =
        "Node already in graph";
    private static final String ABSENT_NODE_ERROR = "Node not in graph";
    private static final String SELF_LINK_ERROR =
        "Node can not be linked to itself";
    private static final String NULL_OBSERVER_ERROR =
        "Observer can not be null";
    private static final String ALREADY_SUBSCRIBED_ERROR =
        "Observer already subscribed";
    private static final String ABSENT_OBSERVER_ERROR =
        "Observer not subscribed";
    private static final String NEGATIVE_RADIUS_ERROR =
        "Radius must be greater than or equal to 0";

    /**
     * Creates a new empty graph.
     */
    public Graph() {
        this.adj = new HashMap<Node, ArrayList<Node>>();
        this.identifier = UUID.randomUUID();
        this.observers = new ArrayList<>();
    }

    /**
     * Returns the number of nodes in this Graph.
     *
     * @return an integer equal to the number of nodes in this Graph.
     */
    public int size() {
        return this.adj.size();
    }

    /**
     * Indicates if the given node is part of this Graph.
     *
     * @param node A node, not null.
     * @return True if and only if node is not null and is part of this graph.
     * @throws NullPointerException if node is null.
     */
    public boolean contains(Node node) {
        Objects.requireNonNull(node, NULL_NODE_ERROR);
        return this.adj.containsKey(node);
    }

    /**
     * Adds a node to this Graph.
     *
     * @param node A node to add, not null.
     * @throws NullPointerException if node is null.
     * @throws IllegalArgumentException if node is already part of this graph.
     */
    public void add(Node node) {
        Objects.requireNonNull(node, NULL_NODE_ERROR);
        if (this.contains(node))
            throw new IllegalArgumentException(NODE_ALREADY_IN_GRAPH_ERROR);
        this.adj.put(node, new ArrayList<>());
        this.updateObservers();
    }

    /**
     * Remove the given node from this Graph.
     *
     * @param node The node to remove, not null and not absent from this graph.
     * @throws NullPointerException if node is null.
     * @throws IllegalArgumentException if node is not part of this graph.
     */
    public void remove(Node node) {
        Objects.requireNonNull(node, NULL_NODE_ERROR);
        if (!this.contains(node))
            throw new IllegalArgumentException(ABSENT_NODE_ERROR);
        this.adj.remove(node);
        this.updateObservers();
    }

    /**
     * Links the first node to the second node.
     *
     * The link is directed, that is, the second node is considered a neighbor
     * of the first node, but the first node is not a neighbor of the second
     * node.
     *
     * @param src The source of the link, not null, not absent.
     * @param dst The destination of the link, not null, not absent.
     * @throws NullPointerException if src or dst are null.
     * @throws IllegalArgumentException if src is absent, or if dst is absent,
     *         or if src is already linked to dst, or if src and dst are equal.
     */
    public void link(Node src, Node dst) {
        Objects.requireNonNull(src, NULL_NODE_ERROR);
        Objects.requireNonNull(dst, NULL_NODE_ERROR);
        if (!this.contains(src) || !this.contains(dst))
            throw new IllegalArgumentException(ABSENT_NODE_ERROR);
        if (src.equals(dst))
            throw new IllegalArgumentException(SELF_LINK_ERROR);
        List<Node> value = this.adj.get(src);
        value.add(dst);
        this.updateObservers();
    }

    /**
     * Returns the list of nodes linked to the given node.
     *
     * The list of nodes linked to the given node is the list of nodes that were
     * passed as destination to a call to link where the source was the
     * given node.
     *
     * @param node A node of the graph, not null, not absent.
     * @return A list of nodes of the graph, the neighbors of node.
     * @throws NullPointerException if node is null.
     * @throws IllegalArgumentException if node is not in graph.
     */
    public List<Node> getNodesLinkedTo(Node node) {
        Objects.requireNonNull(node, NULL_NODE_ERROR);
        if (!this.contains(node))
            throw new IllegalArgumentException(ABSENT_NODE_ERROR);
        List<Node> nodes = this.adj.get(node);
        if (nodes != null) {
            List<Node> copy = new ArrayList<>();
            copy.addAll(nodes);
            return copy;
        }
        return null;
    }

    /**
     * Returns the UUID of this Graph.
     *
     * @return A randomly generated universally unique identifier.
     */
    public UUID getUuid() {
        return this.identifier;
    }

    @Override
    public void subscribe(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        if (this.observers.contains(observer))
            throw new IllegalArgumentException(ALREADY_SUBSCRIBED_ERROR);
        this.observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        this.observers.remove(observer);
    }

    @Override
    public void update(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException(ABSENT_OBSERVER_ERROR);
        observer.updateWithData(this);
    }

    @Override
    public void updateObservers() {
        for (Observer observer: this.observers)
            observer.updateWithData(this);
    }

    /**
     * Returns a list of the nodes in this Graph.
     *
     * @return a list of the nodes in this Graph.
     */
    public List<Node> getNodes() {
        Set<Node> nodes = this.adj.keySet();
        List<Node> nodesAsList = new ArrayList<>();
        nodesAsList.addAll(nodes);
        return nodesAsList;
    }

    /**
     * Returns the first node around the given location, or null if there is
     * none.
     *
     * This function searches for a node inside a circle of given radius around
     * the given location. If there is none, null is returned. If there are
     * multiple candidates, the first node encountered in the traversal is
     * returned.
     * One way to get the node exactly at the given location is to pass a radius
     * equal to 0.
     *
     * @param x The x coordinate of the location.
     * @param y The y coordinate of the location.
     * @param radius The radius of the search circle, must be greater than or
     *        equal to 0.
     * @throws IllegalArgumentException if radius is strictly inferior to 0.
     * @return The first node encountered inside the circle of given radius
     *         around the given location, or null if there is none.
     */
    public Node getNodeAt(int x, int y, int radius) {
        if (radius < 0)
            throw new IllegalArgumentException(NEGATIVE_RADIUS_ERROR);
        for (Node node: this.adj.keySet()) {
            if (node.distanceFrom(x, y) <= radius)
                return node;
        }
        return null;
    }
}
