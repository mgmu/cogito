package cogito.controller;

import java.util.Objects;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import cogito.model.Graph;
import cogito.model.Node;
import cogito.view.GraphView;
import cogito.view.DetailedNodeView;

/**
 * This controller moves the node pressed according to mouse position and
 * populates the detailed node view of the graph editor with the data of the
 * node clicked on.
 */
public class SelectNodeController extends GraphEditorMouseController {

    private DetailedNodeView detailedNodeView;
    private Node pressedNode;

    /**
     * Creates a new SelectNodeController for specified view and model.
     *
     * @param graphView The graph view, not null.
     * @param detailedNodeView The detailed node view, not null.
     * @param model The Graph this controller acts on, not null.
     * @throws NullPointerException if view or model are null.
     */
    public SelectNodeController(
      GraphView graphView,
      DetailedNodeView detailedNodeView,
      Graph model
    ) {
        super(graphView, model);
        this.detailedNodeView = Objects.requireNonNull(detailedNodeView);
        this.pressedNode = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY();
        int[] posInGraph = this.view.getGraphSpacePositionFromScreenPosition(
          clickX,
          clickY
        );
        Node nodeClicked = this.model.getNodeAt(
          posInGraph[0],
          posInGraph[1],
          GraphView.SELECTION_CIRCLE_RADIUS
        );
        System.out.println("nodeClicked is null ? " + (nodeClicked == null));
        if (nodeClicked == null) // click on void
            return;
        this.detailedNodeView.clearModel();
        this.detailedNodeView.setModel(nodeClicked);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (this.pressedNode == null)
            return;
        Rectangle rect = this.view.getRectangleView();
        if (mouseX < 0) {
            this.pressedNode.setX(rect.x);
        } else {
            // -1 to compensate the negative result of the test of interiority
            // made by Rectangle.contains: if the point is strictly on the
            // bounds, it is considered outside, then it is not displayed
            this.pressedNode.setX(
              rect.x + (mouseX > rect.width ? rect.width - 1 : mouseX)
            );
        }
        if (mouseY < 0) {
            this.pressedNode.setY(rect.y);
        } else {
            this.pressedNode.setY(
              // -1: same reasons as above
              rect.y + (mouseY > rect.height ? rect.height - 1: mouseY)
            );
        }
        this.model.updateObservers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int pressX = e.getX();
        int pressY = e.getY();
        int[] posInGraph = this.view.getGraphSpacePositionFromScreenPosition(
          pressX,
          pressY
        );
        Node pressedNode = this.model.getNodeAt(
          posInGraph[0],
          posInGraph[1],
          GraphView.SELECTION_CIRCLE_RADIUS
        );
        if (pressedNode == null)
            return;
        this.pressedNode = pressedNode;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.pressedNode = null;
    }

    @Override
    public void enable() {
        super.enable();
        this.view.showSelectionCircles();
        this.view.stopListeningMouseInput();
    }

    @Override
    public void disable() {
        super.disable();
        this.view.hideSelectionCircles();
        this.view.listenMouseInput();
    }
}
