package cogito.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import cogito.model.Graph;
import cogito.TestUtils;

class GraphViewTest {

    @Nested
    class WithNew {
        GraphView sut;

        @BeforeEach
        void createNewGraphView() {
            sut = new GraphView(new Graph(), 0, 0, null);
        }

        @Test
        void updateOfGraphViewWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Object can not be null",
                    () -> sut.updateWithData(null));
        }

        @Test
        void updateOfGraphViewWithNonNullObjectButNotGraphThrowsIAE() {
            Integer i = 1;
            TestUtils.assertThrowsIAEWithMsg(
              "Object must be an instance of Graph",
              () -> sut.updateWithData(i)
            );
        }
    }
}
