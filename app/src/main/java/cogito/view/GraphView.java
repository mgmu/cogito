package cogito.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
    private ArrayList<Pair<Point, Point>> linkViews;

    // The margin around the title
    private final int TITLE_MARGIN = 5;

    // Preferred dimensions of this GraphView.
    private final int preferredWidth;
    private final int preferredHeight;

    // The frame of the app.
    private JFrame appFrame;

    // Error message to show when a null object is given.
    private static final String NULL_OBJECT_ERROR = "Object can not be null";

    // Error message to show when a given object is not a graph.
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
        this.appFrame = appFrame;
        this.linkViews = new ArrayList<>();
    }

    @Override
    public void updateWithData(Object object) {
        Objects.requireNonNull(object, NULL_OBJECT_ERROR);
        if (!(object instanceof Graph))
            throw new IllegalArgumentException(NOT_A_GRAPH_ERROR);
        this.model = (Graph)object;

        this.repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.preferredWidth, this.preferredHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        for (Node node: this.model.getNodes()) {
            String title = node.getTitle();
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D titleBounds = fontMetrics.getStringBounds(title, g2d);
            int titleWidth = (int)titleBounds.getWidth();
            int titleHeight = (int)titleBounds.getHeight();
            int titleCenterX = node.getX() - titleWidth / 2;
            // drawString needs y coordinate of base line
            int titleCenterY = node.getY() + titleHeight / 2;
            g2d.drawString(title, titleCenterX, titleCenterY);
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
}
