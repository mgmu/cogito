package cogito.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cogito.model.Graph;

class GraphViewTest {

    @Test
    void updateOfGraphViewWithNullThrowsNPE() {
        GraphView sut = new GraphView(new Graph());
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.updateWithData(null));
        assertEquals("Object can not be null", npe.getMessage());
    }

    @Test
    void updateOfGraphViewWithNonNullObjectButNotGraphThrowsIAE() {
        GraphView sut = new GraphView(new Graph());
        Integer i = 1;
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.updateWithData(i)
        );
        assertEquals("Object must be an instance of Graph", iae.getMessage());
    }
}
