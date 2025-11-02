package cogito.controller;

import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import cogito.view.GraphView;
import cogito.model.Graph;

public class ExplorationController extends GraphEditorMouseController {

    public ExplorationController(GraphView view, Graph model) {
        super(view, model);
    }

    @Override
    public void enable() {
        this.view.listenMouseInput();
    }

    @Override
    public void disable() {
        this.view.stopListeningMouseInput();
    }
}
