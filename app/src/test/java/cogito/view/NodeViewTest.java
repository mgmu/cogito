package cogito.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cogito.model.Node;

class NodeViewTest {

    @Test
    void updateOfNodeViewWithNullThrowsNPE() {
        NodeView sut = new NodeView(new Node("test"));
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.updateWithData(null));
        assertEquals("Object can not be null", npe.getMessage());
    }

    @Test
    void updateOfNodeViewWithNonNullObjectOtherThanNodeThrowsIAE() {
        NodeView sut = new NodeView(new Node("test"));
        Integer i = 1;
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.updateWithData(i)
        );
        assertEquals("Object must be an instance of Node", iae.getMessage());
    }
}
