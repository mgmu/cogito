package cogito.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphEditorTest {

    @Test
    void newGraphEditorWithNullModelThrowsNPE() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new GraphEditor(null, null));
        assertEquals("Graph can not be null", npe.getMessage());
    }
}
