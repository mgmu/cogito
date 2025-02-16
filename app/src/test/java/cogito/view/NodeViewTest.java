package cogito.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import cogito.TestUtils;
import cogito.model.Node;

class NodeViewTest {

    @Nested
    class GivenNew {
        NodeView sut;

        @BeforeEach
        void createNewNodeView() {
            sut = new NodeView(new Node("title"));
        }

        @Test
        void whenUpdateWithNullDataThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Object can not be null",
                    () -> sut.updateWithData(null));
        }

        @Test
        void whenUpdateWithDataThatIsNotANodeThrowsIAE() {
            TestUtils.assertThrowsIAEWithMsg("Object is not a Node",
                    () -> sut.updateWithData((Integer)1));
        }

        @Test
        void whenSetGraphics2DWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Graphics2D can not be null",
                    () -> sut.setGraphics2D(null));
        }

        @Test
        void whenComputeTitleDimensionsThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Graphics2D can not be null",
                    () -> sut.computeTitleDimensions());
        }
    }
}
