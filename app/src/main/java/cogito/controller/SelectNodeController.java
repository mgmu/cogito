package cogito.controller;

import java.util.Objects;
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
        Node nodeClicked = this.view.getNodeAt(clickX, clickY);
        if (nodeClicked == null) // click on void
            return;
        if (this.detailedNodeView.updateModel())
            this.detailedNodeView.setModel(nodeClicked);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (this.pressedNode == null)
            return;
        this.pressedNode.setX(mouseX);
        this.pressedNode.setY(mouseY);
        this.model.updateObservers();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int pressX = e.getX();
        int pressY = e.getY();
        Node pressedNode = this.view.getNodeAt(pressX, pressY);
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
    }

    @Override
    public void disable() {
        super.disable();
        this.view.hideSelectionCircles();
    }
}
