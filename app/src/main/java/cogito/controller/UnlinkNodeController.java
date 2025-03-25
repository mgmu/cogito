package cogito.controller;

import java.awt.event.MouseEvent;
import cogito.view.GraphView;
import cogito.model.Graph;
import cogito.model.Node;

/**
 * This controller unlinks two selected nodes of the graph view.
 */
public class UnlinkNodeController extends GraphEditorMouseController {

    // The source of the link.
    private Node src;

    // The destination of the link.
    private Node dst;

    /**
     * Creates a new UnlinkNodeController for specified view and model.
     *
     * @param view The GraphView this controller listens to, not null.
     * @param model The Graph this controller acts on, not null.
     * @throws NullPointerException if view or model are null.
     */
    public UnlinkNodeController(GraphView view, Graph model) {
        super(view, model);
        this.src = null;
        this.dst = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY();
        Node nodeClicked = this.view.getNodeAt(clickX, clickY);
        if (nodeClicked == null) { // click on void
            this.setSrcAndDstToNull();
            this.view.hideSelectedCircle();
        }
        if (this.src == null) {
            this.src = nodeClicked;
            this.view.showSelectedCircle(this.src);
            
        } else if (this.src.equals(nodeClicked)) {
            this.setSrcAndDstToNull();
            this.view.hideSelectedCircle();
        } else {
            this.dst = nodeClicked;
            this.model.unlink(this.src, this.dst);
            this.model.updateObservers();
            this.setSrcAndDstToNull();
        }
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

    private void setSrcAndDstToNull() {
        this.src = null;
        this.dst = null;
    }
}
