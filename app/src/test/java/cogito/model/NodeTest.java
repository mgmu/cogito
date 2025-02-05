package cogito.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import cogito.view.Observer;
import cogito.TestUtils;

class NodeTest {

    class DummyObserver implements Observer {
        boolean updated = false;

        public void update(Object object) {
            updated = true;
        }
    }

    @Test
    void newNodeWithEmptyTitleThrowsException() {
        TestUtils.assertThrowsIAEWithMsg("Node title can not be empty",
                () -> new Node(""));
    }

    @Test
    void newNodeWithTitleLongerThan100CharsThrowsException() {
        TestUtils.assertThrowsIAEWithMsg(
          "Node title can not be longer than 100 characters",
          () -> new Node("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                  + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        );
    }

    @Test
    void newNodeWithTitleInfoDoesNotThrowException() {
        assertDoesNotThrow(() -> new Node("info"));
    }

    @Test
    void getTitleOfNodeWithTitleInfoReturnsInfo() {
        Node sut = new Node("info");
        assertEquals("info", sut.getTitle());
    }

    class WithTitleTest {
        Node sut;

        @BeforeEach
        void newTestNode() {
            sut = new Node("test");
        }

        @Test
        void nodeShouldHaveAGetTitle() {
            assertNotNull(sut.getTitle(),
                    "node class should have a getTitle method");
        }

        @Test
        void getTitleOfNodeWithTitleTestReturnsTest() {
            assertEquals("test", sut.getTitle());
        }

        @Test
        void nodeTitleIsUpdatedWithSetTitle() {
            sut.setTitle("info");
            assertEquals("info", sut.getTitle());
        }

        @Test
        void setTitleWithEmptyStringThrowsException() {
            TestUtils.assertThrowsIAEWithMsg("Node title can not be empty",
                    () -> sut.setTitle(""));
        }

        @Test
        void setTitleWithStringLongerThan100CharsThrowsException() {
            TestUtils.assertThrowsIAEWithMsg(
              "Node title can not be longer than 100 characters",
              () -> sut.setTitle("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      + "aa")
            );
        }

        @Test
        void setTitleWithNullTitleThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Node title can not be null",
                    () -> sut.setTitle(null));
        }

        @Test
        void nodeShouldHaveAGetEntries() {
            assertNotNull(sut.getEntries(),
                    "Node should have a getEntries method");
        }

        @Test
        void getEntriesOnNewNodeReturnsTheEmptyList() {
            assertTrue(sut.getEntries().isEmpty());
        }

        @Test
        void nodeEntriesSizeIncreasesBy1WhenAddsValidEntry() {
            List<String> entries = sut.getEntries();
            assertEquals(0, entries.size());
            sut.add("entry 1");
            entries = sut.getEntries();
            assertEquals(1, entries.size());
        }

        @Test
        void addThrowsNPEIfNewEntryIsNull() {
            TestUtils.assertThrowsNPEWithMsg("Entry can not be null",
                    () -> sut.add(null));
        }

        @Test
        void addThrowsIAEIfEntryIsEmpty() {
            TestUtils.assertThrowsIAEWithMsg("Entry can not be empty",
                    () -> sut.add(""));
        }

        @Test
        void addThrowsIAEIfEntryIsLongerThan500Chars() {
            TestUtils.assertThrowsIAEWithMsg(
              "Entry length can not exceed 500 chars",
              () -> {
                  String longEntry = new String("");
                  for (int i = 0; i <= 500; i++)
                      longEntry += "a";
                  sut.add(longEntry);
              }
            );
        }

        @Test
        void removeWithNegativeValueThrowsIllegalArgumentException() {
            TestUtils.assertThrowsIAEWithMsg("Invalid entry index",
                    () -> sut.remove(-1));
        }

        @Test
        void removeWithIndexOutOfRangeThrowsIllegalArgumentException() {
            sut.add("entry 1");
            sut.add("entry 2");
            TestUtils.assertThrowsIAEWithMsg("Invalid entry index",
                    () -> sut.remove(2));
        }

        @Test
        void removeWithValidIndexRemovesCorrespondingEntry() {
            sut.add("entry 1");
            sut.add("entry 2");
            sut.add("entry 3");
            sut.remove(1);
            List<String> entries = sut.getEntries();
            assertEquals(2, entries.size());
            assertEquals("entry 1", entries.get(0));
            assertEquals("entry 3", entries.get(1));
        }

        
        @Test
        void subscribeWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.subscribe(null));
        }

        @Test
        void updateObserverWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.update(null));
        }

        @Test
        void unsubscribeWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.unsubscribe(null));
        }

        @Test
        void nodeHasAUuidAfterCreation() {
            assertNotNull(sut.getUuid());
        }

        @Nested
        class WithObserver {
            DummyObserver obs;
            
            @BeforeEach
            void createObserver() {
                obs = new DummyObserver();
            }

            @Test
            void setDifferentTitleTriggersUpdate() {
                sut.subscribe(obs);
                sut.setTitle("test1");
                assertTrue(obs.updated);
            }
            
            @Test
            void subscribeObserverTwiceThrowsIAE() {
                sut.subscribe(obs);
                TestUtils.assertThrowsIAEWithMsg("Observer already subscribed",
                        () -> sut.subscribe(obs));
            }

            @Test
            void updateAbsentObserverThrowsIAE() {
                TestUtils.assertThrowsIAEWithMsg("Observer not subscribed",
                        () -> sut.update(obs));
            }

            @Test
            void updateObserverUpdates() {
                sut.subscribe(obs);
                sut.update(obs);
                assertTrue(obs.updated);
            }

            @Test
            void addEntryUpdatesObservers() {
                DummyObserver obs2 = new DummyObserver();
                sut.subscribe(obs);
                sut.subscribe(obs2);
                sut.add("entry1");
                assertTrue(obs.updated);
                assertTrue(obs2.updated);
            }

            @Test
            void removeEntryUpdatesObservers() {
                DummyObserver obs2 = new DummyObserver();
                sut.add("entry1");
                sut.subscribe(obs);
                sut.subscribe(obs2);
                sut.remove(0);
                assertTrue(obs.updated);
                assertTrue(obs2.updated);
            }
        }
    }

    @Test
    void newNodeWithNullTitleThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node title can not be null",
                () -> new Node(null));
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
        TestUtils.assertThrowsNPEWithMsg(
          "New node can not be initialized with null list",
          () -> new Node("test", null)
        );
    }

    @Test
    void newNodeWithNullEntriesThrowsNPE() {
        List<String> dummyEntries = new ArrayList<>();
        dummyEntries.add("not null");
        dummyEntries.add(null);
        dummyEntries.add("not null again");
        TestUtils.assertThrowsNPEWithMsg("Entry can not be null",
                () -> new Node("test", dummyEntries));
    }

    @Test
    void newNodeWithMoreThan500EntriesThrowsIllegalArgumentException() {
        List<String> dummyEntries = new ArrayList<>();
        for (int i = 0; i <= 500; i++)
            dummyEntries.add("a");
        TestUtils.assertThrowsIAEWithMsg("Node can contain at most 500 entries",
                () -> new Node("test", dummyEntries));
    }

    @Test
    void newNodeWithEmtpyEntryThrowsIAE() {
        List<String> dummyEntries = new ArrayList<>();
        dummyEntries.add("");
        TestUtils.assertThrowsIAEWithMsg("Entry can not be empty",
                () -> new Node("test", dummyEntries));
    }

    @Test
    void newNodeWithEntryLongerThan500CharsThrowsIAE() {
        List<String> dummyEntries = new ArrayList<>();
        String test = new String("");
        for (int i = 0; i <= 500; i++)
            test = test + "a";
        dummyEntries.add(test);
        TestUtils.assertThrowsIAEWithMsg(
          "Entry length can not exceed 500 chars",
          () -> new Node("test", dummyEntries)
        );
    }
}
