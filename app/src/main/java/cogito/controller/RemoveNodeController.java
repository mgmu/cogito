package cogito.controller;

import javax.swing.JOptionPane;
import cogito.model.Graph;
import cogito.model.Node;
import cogito.view.GraphView;
import java.awt.event.MouseEvent;

public class RemoveNodeController extends GraphEditorMouseController {
    
    public RemoveNodeController(GraphView view, Graph model) {
        super(view, model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY();
        Node nodeClicked = this.view.getNodeAt(clickX, clickY);
        if (nodeClicked == null)
            return;
        int choice = JOptionPane.showConfirmDialog(
          this.view.getAppFrame(),
          "The node will be permantly deleted, proceed?",
          "Delete node",
          JOptionPane.YES_NO_OPTION
        );
        if (choice == -1 || choice == 1) // cancel/no
            return;
        this.model.remove(nodeClicked);
        this.model.updateObservers();
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
