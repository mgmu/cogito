package cogito.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PairTest {
    
    @Test
    void newPairWithNullKeyThrowsNPE() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new Pair<Object, Object>(null, null));
        assertEquals("Key can not be null", npe.getMessage());
    }

    @Test
    void newPairWithNullValueThrowsNPE() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new Pair<Object, Object>(new Object(), null));
        assertEquals("Value can not be null", npe.getMessage());
    }

    @Test
    void getKeyReturnsKey() {
        Pair<Integer, Integer> sut = new Pair<>(1, 2);
        assertEquals(1, sut.getKey());
    }

    @Test
    void getValueReturnsValue() {
        Pair<Integer, Integer> sut = new Pair<>(1, 2);
        assertEquals(2, sut.getValue());
    }

    @Test
    void equalsWithSameObjectReturnsTrue() {
        Pair<Integer, Integer> sut = new Pair<>(1, 2);
        assertTrue(sut.equals(sut));
    }

    @Test
    void equalsWithNullReturnsFalse() {
        Pair<Integer, Integer> sut = new Pair<>(1, 2);
        assertFalse(sut.equals(null));
    }

    @Test
    void equalsWithIntegerReturnsFalse() {
        Pair<Integer, Integer> sut = new Pair<>(1, 2);
        assertFalse(sut.equals(1));
    }

    @Test
    void equalsWithDifferentPairReturnsFalse() {
        Pair<Integer, Integer> sut1 = new Pair<>(1, 2);
        Pair<Integer, Integer> sut2 = new Pair<>(2, 1);
        assertFalse(sut1.equals(sut2));
    }

    @Test
    void equalsWithEqualPairReturnsTrue() {
        Pair<Integer, Integer> sut1 = new Pair<>(1, 2);
        Pair<Integer, Integer> sut2 = new Pair<>(1, 2);
        assertTrue(sut1.equals(sut2));
    }
}
