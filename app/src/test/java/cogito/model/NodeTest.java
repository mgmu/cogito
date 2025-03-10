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

        public void updateWithData(Object object) {
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
    void newNodeWithNullTitleThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node title can not be null",
                () -> new Node(null));
    }

    @Test
    void newNodeWithNullInformationThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Textual information can not be null",
                () -> new Node("test", null));
    }

    @Test
    void newNodeWithLongInformationThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg(
          "Text length can not exceed 5000 characters",
          () -> {
              String longInfo = "";
              for (int i = 0; i <= 5000; i++)
                  longInfo += "a";
              new Node("test", longInfo);
          }
        );
    }

    @Nested
    class AtLocationX5Y10 {
        Node sut;

        @BeforeEach
        void newTestNodeAtLocation() {
            sut = new Node("test", 5, 10);
        }

        @Test
        void nodeXShouldBe5() {
            assertEquals(5, sut.getX());
        }

        @Test
        void nodeYShouldBe10() {
            assertEquals(10, sut.getY());
        }

        @Test
        void distanceFrom00Is11dot18() {
            assertEquals(11.18, sut.distanceFrom(0, 0));
        }

        @Test
        void getInformationOfNodeWithNoInformationReturnsTheEmptyString() {
            assertEquals("", sut.getInformation());
        }

        @Test
        void setInformationTooLongThowsIAE() {
            TestUtils.assertThrowsIAEWithMsg(
              "Text length can not exceed 5000 characters", 
              () -> {
                  String longInfo = "";
                  for (int i = 0; i <= 5000; i++)
                      longInfo += "a";
                  sut.setInformation(longInfo);
              }
            );
        }

        @Nested
        class AfterSetXAndY {

            @BeforeEach
            void setNewLocation() {
                sut.setX(-10);
                sut.setY(50);
            }

            @Test
            void nodeXShouldBeMinus10() {
                assertEquals(-10, sut.getX());
            }

            @Test
            void nodeYShouldBe50() {
                assertEquals(50, sut.getY());
            }
        }
    }
        
    @Nested
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

        @Test
        void nodeIsAtXLocation0() {
            assertEquals(0, sut.getX());
        }

        @Test
        void nodeIsAtYLocation0() {
            assertEquals(0, sut.getY());
        }

        @Nested
        class WithObserver {
            DummyObserver obs;
            
            @BeforeEach
            void createObserver() {
                obs = new DummyObserver();
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
        }
    }
}
