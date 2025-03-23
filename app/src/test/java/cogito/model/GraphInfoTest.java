package cogito.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import cogito.TestUtils;
import java.util.UUID;

class GraphInfoTest {

    @Test
    void newGraphInfoWithNullNameThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg(
          "Name can not be null.",
          () -> new GraphInfo(null, null)
        );
    }

    @Test
    void newGraphInfoWithNullUuidThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg(
          "Identifier can not be null.",
          () -> new GraphInfo("name", null)
        );
    }

    @Test
    void graphInfoToStringReturnsName() {
        GraphInfo sut = new GraphInfo("test", UUID.randomUUID());
        assertEquals("test", sut.toString());
    }
}
