package cogito;

import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {

    public static void assertThrowsNPEWithMsg(
      String errorMessage, Executable executable) {
        NullPointerException npe = assertThrows(NullPointerException.class,
                executable);
        assertEquals(errorMessage, npe.getMessage());
    }

    public static void assertThrowsIAEWithMsg(
      String errorMessage, Executable executable) {
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          executable
        );
        assertEquals(errorMessage, iae.getMessage());
    }
}
