package cogito.controller;

import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import cogito.model.Graph;
import cogito.model.Node;
import cogito.view.GraphView;

/**
 * This controller creates a node at the location of the mouse click to the
 * graph model.
 */
public class AddNodeController extends GraphEditorMouseController {

    /**
     * Creates a new AddNodeController for specified view and model.
     *
     * @param view The GraphView this controller listens to, not null.
     * @param model The Graph this controller acts on, not null.
     * @throws NullPointerException if view or model are null.
     */
    public AddNodeController(GraphView view, Graph model) {
        super(view, model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int centerX = e.getX();
        int centerY = e.getY();
        String input = (String)JOptionPane.showInputDialog(
          this.view.getAppFrame(),
          "Enter new node name:",
          "New node",
          JOptionPane.PLAIN_MESSAGE,
          null,
          null,
          ""
        );
        if (input == null)
            return;
        try {
            this.model.add(
              new Node(input, centerX, centerY)
            );
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(
              this.view.getAppFrame(),
              iae.getMessage(),
              "Node could not be added",
              JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
