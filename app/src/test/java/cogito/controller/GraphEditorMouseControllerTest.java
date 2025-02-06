package cogito.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import cogito.TestUtils;
import cogito.model.Graph;
import cogito.view.GraphView;

class GraphEditorMouseControllerTest {
    
    class DummyGEMC extends GraphEditorMouseController {
        
        DummyGEMC(GraphView view, Graph model) {
            super(view, model);
        }
    }

    @Test
    void newGEMCWithNullViewThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("GraphView can not be null",
                () -> new DummyGEMC(null, null));
    }

    @Test
    void newGEMCWithNullModelThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Graph can not be null",
                () -> new DummyGEMC(new GraphView(), null));
    }
}
