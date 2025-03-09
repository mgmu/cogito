package cogito.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.FontMetrics;
import cogito.util.Pair;
import cogito.model.Graph;
import cogito.model.Node;

/**
 * View of the graph model.
 *
 * This view represents the nodes as circles with their title inside and the
 * relations between the nodes.
 */
public class GraphView extends JPanel implements Observer {

    // The model represented by this GraphView.
    private Graph model;
    private List<NodeView> nodeViews;
    private ArrayList<Pair<Point, Point>> linkViews;

    // Preferred dimensions of this GraphView.
    private final int preferredWidth;
    private final int preferredHeight;
    private boolean isSelectionCircleVisible;

    // The frame of the app.
    private JFrame appFrame;

    // The radius of the selection circle.
    private static final int SELECTION_CIRCLE_RADIUS = 10;

    // Error messages.
    private static final String NULL_OBJECT_ERROR = "Object can not be null";
    private static final String NOT_A_GRAPH_ERROR =
        "Object must be an instance of Graph";

    /**
     * Creates a new graph view of the specified model.
     *
     * @param model The graph model to represent.
     * @param width The preferred width of this GraphView.
     * @param height The preferred height of this GraphView.
     * @param appFrame The application JFrame.
     */
    public GraphView(Graph model, int width, int height, JFrame appFrame) {
        this.model = Objects.requireNonNull(model, "Graph can not be null");
        this.preferredWidth = width;
        this.preferredHeight = height;
        this.isSelectionCircleVisible = false;
        this.appFrame = appFrame;
        this.nodeViews = new ArrayList<>();
        this.loadNodeViews();
        this.linkViews = new ArrayList<>();
        this.loadLinkViews();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public void updateWithData(Object object) {
        Objects.requireNonNull(object, NULL_OBJECT_ERROR);
        if (!(object instanceof Graph))
            throw new IllegalArgumentException(NOT_A_GRAPH_ERROR);
        this.model = (Graph)object;
        for (NodeView nv: this.nodeViews) {
            nv.getModel().unsubscribe(nv);
        }
        this.nodeViews.clear();
        this.loadNodeViews();
        this.linkViews.clear();
        this.loadLinkViews();
        this.repaint();
    }

    // Loads the node views
    private void loadNodeViews() {
        for (Node node: this.model.getNodes()) {
            NodeView nv = new NodeView(node);
            node.subscribe(nv);
            this.nodeViews.add(nv);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        for (NodeView nv: this.nodeViews) {
            if (this.isSelectionCircleVisible) {
                int upperLeftX = nv.getModel().getX() - SELECTION_CIRCLE_RADIUS;
                int upperLeftY = nv.getModel().getY() - SELECTION_CIRCLE_RADIUS;
                int diameter = SELECTION_CIRCLE_RADIUS * 2;
                Shape selectionCircle = new Ellipse2D.Double(
                  upperLeftX,
                  upperLeftY,
                  diameter,
                  diameter
                );
                g2d.draw(selectionCircle);
            }
            String title = nv.getModel().getTitle();
            nv.setGraphics2D(g2d);
            nv.computeTitleDimensions();
            g2d.drawString(
              title,
              nv.getTitleBaseLineX(),
              nv.getTitleBaseLineY()
            );
        }

        for (Pair<Point, Point> link: this.linkViews) {
            // draw links
            Point src = link.getKey();
            Point dst = link.getValue();
            Line2D.Double line = new Line2D.Double(src.x, src.y, dst.x, dst.y);
            g2d.draw(line);
        }
    }

    /**
     * Returns the application frame.
     *
     * @return The application JFrame.
     */
    public JFrame getAppFrame() {
        return this.appFrame;
    }

    // Populates linkViews with segments corresponding to node links
    private void loadLinkViews() {
        for (Node node: this.model.getNodes()) {
            List<Node> neighbors = this.model.getNodesLinkedTo(node);
            Point src = new Point(node.getX(), node.getY());
            for (Node neighbor: neighbors) {
                Point dst = new Point(neighbor.getX(), neighbor.getY());
                this.linkViews.add(new Pair<>(src, dst));
            }
        }
    }

    /**
     * Returns the node at given location.
     *
     * The location is a point inside the circle around the center of the title.
     * If there are multiple candidates, the first one encountered in the
     * traversal of the nodes of the model is returned.
     *
     * @param x The x coordinate of the click.
     * @param y The y coordinate of the click.
     * @return The Node clicked on, or null if the click was in the void.
     */
    public Node getNodeAt(int x, int y) {
        return this.model.getNodeAt(x, y, SELECTION_CIRCLE_RADIUS);
    }

    /**
     * Shows selection circles around node views.
     */
    public void showSelectionCircles() {
        this.isSelectionCircleVisible = true;
        this.repaint();
    }

    /**
     * Hides selection circles around node views.
     */
    public void hideSelectionCircles() {
        this.isSelectionCircleVisible = false;
        this.repaint();
    }
}
