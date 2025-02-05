package cogito.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cogito.TestUtils;

class GraphEditorTest {

    @Test
    void newGraphEditorWithNullModelThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Graph can not be null",
                () -> new GraphEditor(null, null));
    }
}
