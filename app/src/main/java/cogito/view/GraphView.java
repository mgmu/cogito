package cogito.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import cogito.util.Pair;
import cogito.model.Graph;
import cogito.model.Node;

/**
 * View of the graph model.
 *
 * This view represents the nodes as circles with their title inside and the
 * relations between the nodes.
 */
public class GraphView extends JPanel implements Observer,
                                                 MouseMotionListener,
                                                 MouseListener {

    // The model represented by this GraphView.
    private Graph model;

    // The part of the graph that is visible in the rectangle view, as a Graph.
    private Map<Node, ArrayList<Node>> visibleModel;

    // The node views of the nodes of the graph model.
    private List<NodeView> nodeViews;

    // The link views of the links of the graph model.
    private ArrayList<Pair<Point, Point>> linkViews;

    // The selected node view.
    private NodeView selectedNodeView;

    //Preferred width of this GraphView.
    private final int preferredWidth;

    // Preferred height of this GraphView.
    private final int preferredHeight;

    // Indicates if the selection circles around all displayed nodes are
    // visible.
    private boolean isSelectionCircleVisible;

    // The frame of the app.
    private JFrame appFrame;

    // The visible part of the graph space of this graph view model.
    private Rectangle rect;

    // exploration variables
    private int originX = 0;
    private int originY = 0;
    private int endX = 0;
    private int endY = 0;
    private int[] translationVector = new int[2];
    private Rectangle beginTransRect; // current rectangle at beginning of trans

    // The radius of the selection circle.
    public static final int SELECTION_CIRCLE_RADIUS = 10;

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

        // Rectangle view
        this.rect = new Rectangle(0, 0, width, height);
        this.beginTransRect = new Rectangle(this.rect);

        this.visibleModel = this.model.getSubGraphInRectangle(this.rect);

        this.preferredWidth = width;
        this.preferredHeight = height;
        this.isSelectionCircleVisible = false;
        this.appFrame = appFrame;
        this.nodeViews = new ArrayList<>();
        this.loadNodeViews();
        this.linkViews = new ArrayList<>();
        this.loadLinkViews();
        this.selectedNodeView = null;
        
        // Layout
        Border loweredBorder = BorderFactory.createLoweredBevelBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        this.setBorder(
          BorderFactory.createCompoundBorder(loweredBorder, emptyBorder)
        );

        this.listenMouseInput();
    }

    @Override
    public void updateWithData(Object object) {
        Objects.requireNonNull(object, NULL_OBJECT_ERROR);
        if (!(object instanceof Graph))
            throw new IllegalArgumentException(NOT_A_GRAPH_ERROR);
        this.model = (Graph)object;
        this.updateRectangleView(this.rect);
    }

    // Loads the node views
    private void loadNodeViews() {
        for (Node node: this.visibleModel.keySet()) {
            NodeView nv = new NodeView(node, this);
            node.subscribe(nv);
            this.nodeViews.add(nv);
        }
    }

    // Populates linkViews with segments corresponding to node links
    private void loadLinkViews() {
        for (Node node: this.visibleModel.keySet()) {
            ArrayList<Node> neighbors = this.visibleModel.get(node);
            Point src = new Point(node.getX(), node.getY());
            for (Node neighbor: neighbors) {
                Point dst = new Point(neighbor.getX(), neighbor.getY());
                this.linkViews.add(new Pair<>(src, dst));
            }
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
        Font currentFont = g2d.getFont();
        Font increasedFont = currentFont.deriveFont(
          currentFont.getSize() * 1.5f
        );
        g2d.setFont(increasedFont);

        for (Pair<Point, Point> link: this.linkViews)
            this.drawLink(link, g2d);

        for (NodeView nv: this.nodeViews)
            this.drawNode(nv, g2d);
    }

    // called by paintComponent only
    private void drawNode(NodeView nv, Graphics2D g2d) {
        if (this.isSelectionCircleVisible) {
            g2d.setColor(Color.GRAY);
            int upperLeftX = nv.getModel().getX()
                - this.rect.x // rect view might have moved
                - SELECTION_CIRCLE_RADIUS;  // radius of the selection circle
            int upperLeftY = nv.getModel().getY()
                - this.rect.y
                - SELECTION_CIRCLE_RADIUS 
                + 2; // ?
            int diameter = SELECTION_CIRCLE_RADIUS * 2;
            Shape selectionCircle = new Ellipse2D.Double(
              upperLeftX,
              upperLeftY,
              diameter,
              diameter
            );
            if (nv.equals(this.selectedNodeView)) {
                Color prevColor = g2d.getColor();
                g2d.setColor(Color.BLUE);
                g2d.draw(selectionCircle);
                g2d.setColor(prevColor);
            } else
                g2d.draw(selectionCircle);
        }
        g2d.setColor(Color.BLACK);
        String title = nv.getModel().getTitle();
        nv.setGraphics2D(g2d);
        nv.computeTitleDimensions();
        g2d.drawString(
          title,
          nv.getTitleBaseLineX() - this.rect.x,
          nv.getTitleBaseLineY() - this.rect.y
        );
    }

    private void drawLink(Pair<Point, Point> link, Graphics2D g2d) {
        Point src = link.getKey();
        Point dst = link.getValue();
        Line2D.Double line = new Line2D.Double(
          src.x - this.rect.x,
          src.y - this.rect.y,
          dst.x - this.rect.x,
          dst.y - this.rect.y
        );
        g2d.setColor(Color.GRAY);
        g2d.draw(line);

        // draw arrow head, credits to papa
        double L = 10.0;
        
        double dX = dst.x - src.x;
        double dY = dst.y - src.y;

        double vX = dX / Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        double vY = dY / Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        double d = L / Math.sqrt(2);

        double nT = 0.5 * Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) - d;
        double nX = (src.x - this.rect.x) + nT * vX;
        double nY = (src.y - this.rect.y) + nT * vY;

        double aX = nX - d * vY;
        double aY = nY + d * vX;

        double bX = nX + d * vY;
        double bY = nY - d * vX;

        double mX = (dst.x + src.x - 2 * this.rect.x) / 2.0;
        double mY = (dst.y + src.y - 2 * this.rect.y) / 2.0;

        Line2D.Double aToM = new Line2D.Double(aX, aY, mX, mY);
        Line2D.Double bToM = new Line2D.Double(bX, bY, mX, mY);

        g2d.draw(aToM);
        g2d.draw(bToM);
    }

    /**
     * Returns the application frame.
     *
     * @return The application JFrame.
     */
    public JFrame getAppFrame() {
        return this.appFrame;
    }

    /**
     * Returns the position in the graph space that corresponds to the given
     * screen position.
     *
     * @param x The x coordinate of the screen position.
     * @param y The y coordinate of the screen position.
     * @return An array of size 2 where the value at index 0 is the X coordinate
     * in the graph space and the value at index 1 is the Y coordinate in the
     * graph space.
     */
    public int[] getGraphSpacePositionFromScreenPosition(int x, int y) {
        int[] res = new int[2];
        res[0] = x + this.rect.x;
        res[1] = y + this.rect.y;
        return res;
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

    /**
     * Searches the node view that represents the given model, sets it as
     * selected and repaints the graph.
     *
     * If no node view represents the given model, does nothing.
     *
     * @param model The node model of the selected node view.
     */
    public void showSelectedCircle(Node model) {
        for (NodeView nv: this.nodeViews) {
            if (nv.getModel().equals(model)) {
                this.selectedNodeView = nv;
                this.repaint();
                return;
            }
        }
    }

    /**
     * Hides the circle around a node indicating that is selected in the graph
     * editor.
     */
    public void hideSelectedCircle() {
        this.selectedNodeView = null;
        this.repaint();
    }

    /**
     * Returns a copy of this GraphView's rectangle view.
     *
     * @return A Rectangle that is a copy of this GraphView's rectangle view.
     */
    public Rectangle getRectangleView() {
        return new Rectangle(this.rect);
    }

    // Unsubscribes all node views, clears all node views and link views and
    // reloads them from the subgraph.
    private void refresh() {
        for (NodeView nv: this.nodeViews) {
            nv.getModel().unsubscribe(nv);
        }
        this.nodeViews.clear();
        this.loadNodeViews();
        this.linkViews.clear();
        this.loadLinkViews();
        this.repaint();
    }

    /**
     * Updates the rectangle view and this graph view accordingly.
     */
    public void updateRectangleView(Rectangle newRect) {
        this.rect = new Rectangle(newRect);
        this.visibleModel = this.model.getSubGraphInRectangle(this.rect);
        this.refresh();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // does nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // does nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // does nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.originX = e.getX();
        this.originY = e.getY();
        this.beginTransRect = new Rectangle(this.rect);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.translationVector[0] = 0;
        this.translationVector[1] = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.endX = e.getX();
        this.endY = e.getY();
        this.translationVector[0] = -(this.endX - this.originX);
        this.translationVector[1] = -(this.endY - this.originY);
        // update rectangle view
        Rectangle curr = new Rectangle(this.beginTransRect);
        curr.translate(
          this.translationVector[0],
          this.translationVector[1]
        );
        this.updateRectangleView(curr);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // does nothing
    }

    /**
     * Adds this GraphView as a mouse listener and mouse motion listener to this
     * GraphView.
     */
    public void listenMouseInput() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Removes this GraphView from the mouse listeners and mouse motion
     * listeners of this GraphView.
     */
    public void stopListeningMouseInput() {
        this.removeMouseListener(this);
        this.removeMouseMotionListener(this);
    }
}
