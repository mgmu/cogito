package cogito.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

class NodeTest {

    @Test
    void newNodeWithEmptyTitleThrowsException() {
        IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class, () -> {
              Node node = new Node("");
          }
        );
        assertEquals("Node title can not be empty", exception.getMessage());
    }

    @Test
    void newNodeWithTitleLongerThan100CharsThrowsException() {
        IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class, () -> {
              Node node = new Node("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
              "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
              // 101 chars
          }
        );
        assertEquals("Node title can not be longer than 100 characters",
                exception.getMessage());
    }

    @Test
    void newNodeWithTitleInfoDoesNotThrowException() {
        assertDoesNotThrow(() -> new Node("info"));
    }

    @Test
    void nodeShouldHaveAGetTitle() {
        Node sut = new Node("test");
        assertNotNull(sut.getTitle(),
                "node class should have a getTitle method");
    }

    @Test
    void getTitleOfNodeWithTitleInfoReturnsInfo() {
        Node sut = new Node("info");
        assertEquals("info", sut.getTitle());
    }

    @Test
    void getTitleOfNodeWithTitleTestReturnsTest() {
        Node sut = new Node("test");
        assertEquals("test", sut.getTitle());
    }

    @Test
    void nodeTitleIsUpdatedWithSetTitle() {
        Node sut = new Node("test");
        assertEquals("test", sut.getTitle());
        sut.setTitle("info");
        assertEquals("info", sut.getTitle());
    }

    @Test
    void setTitleWithEmptyStringThrowsException() {
        Node sut = new Node("test");
        IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> sut.setTitle("")
        );
        assertEquals("Node title can not be empty", exception.getMessage());
    }

    @Test
    void setTitleWithStringLongerThan100CharsThrowsException() {
        Node sut = new Node("test");
        IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> sut.setTitle("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
              "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        );
        assertEquals("Node title can not be longer than 100 characters",
                exception.getMessage());
    }

    @Test
    void newNodeWithNullTitleThrowsNPE() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new Node(null));
        assertEquals("Node title can not be null", npe.getMessage());
    }

    @Test
    void setTitleWithNullTitleThrowsNPE() {
        Node sut = new Node("test");
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.setTitle(null));
        assertEquals("Node title can not be null", npe.getMessage());
    }

    @Test
    void nodeShouldHaveAGetEntries() {
        Node sut = new Node("test");
        assertNotNull(sut.getEntries(), "Node should have a getEntries method");
    }

    @Test
    void getEntriesOnNewNodeReturnsTheEmptyList() {
        Node sut = new Node("test");
        assertTrue(sut.getEntries().isEmpty());
    }

    @Test
    void getEntriesOnNodeWith2EntriesReturnsAListOfSize2() {
        List<String> entries = new ArrayList<>();
        entries.add("entry 1");
        entries.add("entry 2");
        Node sut = new Node("test", entries);
        assertEquals(2, sut.getEntries().size());
    }

    @Test
    void newNodeWithNullListThrowsNPE() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new Node("test", null));
        assertEquals("New node can not be initialized with null list",
                npe.getMessage());
    }

    @Test
    void newNodeWithNullEntriesThrowsNPE() {
        List<String> dummyEntries = new ArrayList<>();
        dummyEntries.add("not null");
        dummyEntries.add(null);
        dummyEntries.add("not null again");
        NullPointerException npe = assertThrows(
          NullPointerException.class,
          () -> new Node("test", dummyEntries)
        );
        assertEquals("Entry can not be null", npe.getMessage());
    }

    @Test
    void newNodeWithMoreThan500EntriesThrowsIllegalArgumentException() {
        List<String> dummyEntries = new ArrayList<>();
        for (int i = 0; i <= 500; i++)
            dummyEntries.add("a");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> new Node("test", dummyEntries)
        );
        assertEquals("Node can contain at most 500 entries", iae.getMessage());
    }

    @Test
    void newNodeWithEmtpyEntryThrowsIAE() {
        List<String> dummyEntries = new ArrayList<>();
        dummyEntries.add("");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> new Node("test", dummyEntries)
        );
        assertEquals("Entry can not be empty", iae.getMessage());
    }

    @Test
    void newNodeWithEntryLongerThan500CharsThrowsIAE() {
        List<String> dummyEntries = new ArrayList<>();
        String test = new String("");
        for (int i = 0; i <= 500; i++)
            test = test + "a";
        dummyEntries.add(test);
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> new Node("test", dummyEntries)
        );
        assertEquals("Entry length can not exceed 500 chars", iae.getMessage());
    }

    @Test
    void nodeEntriesSizeIncreasesBy1WhenAddsValidEntry() {
        Node sut = new Node("test");
        List<String> entries = sut.getEntries();
        assertEquals(0, entries.size());
        sut.add("entry 1");
        entries = sut.getEntries();
        assertEquals(1, entries.size());
    }

    @Test
    void addThrowsNPEIfNewEntryIsNull() {
        Node sut = new Node("test");
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.add(null));
        assertEquals("Entry can not be null", npe.getMessage());
    }

    @Test
    void addThrowsIAEIfEntryIsEmpty() {
        Node sut = new Node("test");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.add("")
        );
        assertEquals("Entry can not be empty", iae.getMessage());
    }

    @Test
    void addThrowsIAEIfEntryIsLongerThan500Chars() {
        Node sut = new Node("test");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> {
              String longEntry = new String("");
              for (int i = 0; i <= 500; i++)
                  longEntry += "a";
              sut.add(longEntry);
          }
        );
        assertEquals("Entry length can not exceed 500 chars", iae.getMessage());
    }

    @Test
    void removeWithNegativeValueThrowsIllegalArgumentException() {
        Node sut = new Node("test");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.remove(-1)
        );
        assertEquals("Invalid entry index", iae.getMessage());
    }

    @Test
    void removeWithIndexOutOfRangeThrowsIllegalArgumentException() {
        Node sut = new Node("test");
        sut.add("entry 1");
        sut.add("entry 2");
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.remove(2)
        );
        assertEquals("Invalid entry index", iae.getMessage());
    }

    @Test
    void removeWithValidIndexRemovesCorrespondingEntry() {
        Node sut = new Node("test");
        sut.add("entry 1");
        sut.add("entry 2");
        sut.add("entry 3");
        sut.remove(1);
        List<String> entries = sut.getEntries();
        assertEquals(2, entries.size());
        assertEquals("entry 1", entries.get(0));
        assertEquals("entry 3", entries.get(1));
    }
}
