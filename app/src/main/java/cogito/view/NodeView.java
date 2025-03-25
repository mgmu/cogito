package cogito.view;

import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import cogito.model.Node;

/**
 * The view of a node as part of a graph.
 *
 * In order to compute the dimensions of the title when drawn, this class needs
 * a Graphics2D object, given by the drawing class.
 */
public class NodeView implements Observer {
    
    // The model of this view
    private Node model = null;

    // The GraphView where this NodeView is displayed
    private GraphView graphView;

    // The Graphics2D object used to compute the dimensions of the model's title
    private Graphics2D g2d = null;

    // The dimensions of the title
    private int titleWidth = 0;
    private int titleHeight = 0;

    // The position at which to draw the title
    private int titleBaseLineX = 0;
    private int titleBaseLineY = 0;

    /**
     * Creates a new NodeView with given model.
     *
     * @param model The model of this NodeView, not null.
     * @param graphView The graph view of the graph editor, not null.
     * @throws NullPointerException if model or graphView are null.
     */
    public NodeView(Node model, GraphView graphView) {
        this.model = Objects.requireNonNull(model, "Node can not be null");
        this.graphView = Objects.requireNonNull(graphView,
                "GraphView can not be null");
    }

    @Override
    public void updateWithData(Object object) {
        Objects.requireNonNull(object, "Object can not be null");
        if (!(object instanceof Node))
            throw new IllegalArgumentException("Object is not a Node");
        this.model = (Node)object;
        this.graphView.repaint();
    }

    /**
     * Sets the Graphics2D object used to compute the title dimensions.
     *
     * @param g2d A Graphics2D object, not null.
     * @throws NullPointerException if g2d is null.
     */
    public void setGraphics2D(Graphics2D g2d) {
        this.g2d = Objects.requireNonNull(g2d, "Graphics2D can not be null");
    }

    /**
     * Returns the Graphics2D object used to compute the title dimensions.
     *
     * @return The Graphics2D object used to compute the title dimensions.
     */
    public Graphics2D getGraphics2D() {
        return this.g2d;
    }

    /**
     * Computes the title dimensions of the model of this node view.
     *
     * This method must be called before any other method that returns an
     * information about the title dimensions, otherwise the behavior is
     * undefined.
     */
    public void computeTitleDimensions() {
        Objects.requireNonNull(this.g2d, "Graphics2D can not be null");
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D titleBounds = fontMetrics.getStringBounds(
          this.model.getTitle(),
          g2d
        );
        this.titleWidth = (int)titleBounds.getWidth();
        this.titleHeight = (int)titleBounds.getHeight();
        this.titleBaseLineX = this.model.getX() - titleWidth / 2;
        this.titleBaseLineY = this.model.getY() + titleHeight / 2;
    }

    /**
     * Returns the title width when drawn on the graph view.
     *
     * The behavior is undefined if computeTitleDimensions has not been called
     * first.
     *
     * @return The width of the title in pixels.
     */
    public int getTitleWidth() {
        return this.titleWidth;
    }

    /**
     * Returns the title height when drawn on the graph view.
     *
     * The behavior is undefined if computeTitleDimensions has not been called
     * first.
     *
     * @return The height of the title in pixels.
     */
    public int getTitleHeight() {
        return this.titleHeight;
    }

    /**
     * Returns the x coordinate of the base line of the title when drawn on the
     * graph view.
     *
     * The behavior is undefined if computeTitleDimensions has not been called
     * first.
     *
     * @return The x coordinate of the base line of the title in pixels.
     */
    public int getTitleBaseLineX() {
        return this.titleBaseLineX;
    }

    /**
     * Returns the y coordinate of the base line of the title when drawn on the
     * graph view.
     *
     * The behavior is undefined if computeTitleDimensions has not been called
     * first.
     *
     * @return The y coordinate of the base line of the title in pixels.
     */
    public int getTitleBaseLineY() {
        return this.titleBaseLineY;
    }

    /**
     * Returns the model of this node view.
     *
     * @return A Node, the model of this view.
     */
    public Node getModel() {
        return this.model;
    }
}
