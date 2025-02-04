package cogito.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import cogito.view.Observer;
import cogito.TestUtils;

class GraphTest {
    Graph sut;

    class DummyObserver implements Observer {
        boolean updated = false;

        public void update(Object object) {
            updated = true;
        }
    }

    @Nested
    class WhenNew {

        @BeforeEach
        void createNewGraph() {
            sut = new Graph();
        }

        @Test
        void containsWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.contains(null));
        }

        @Test
        void containsWithAbsentNodeReturnsFalse() {
            assertFalse(sut.contains(new Node("test")));
        }

        @Test
        void addWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.add(null));
        }

        @Test
        void sizeOfNewGraphIs0() {
            assertEquals(0, sut.size());
        }

        @Test
        void removeWithNullNodeThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.remove(null));
        }

        @Test
        void removeWithAbsentNodeThrowsIAE() {
            TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                    () -> sut.remove(new Node("test")));
        }

        @Test
        void graphContainsCorrectNodesAfterRemoval() {
            Node n1 = new Node("1");
            Node n2 = new Node("2");
            Node n3 = new Node("3");
            sut.add(n1);
            sut.add(n2);
            sut.add(n3);
            assertEquals(3, sut.size());
            assertTrue(sut.contains(n1));
            assertTrue(sut.contains(n2));
            assertTrue(sut.contains(n3));
            sut.remove(n2);
            assertEquals(2, sut.size());
            assertTrue(sut.contains(n1));
            assertTrue(sut.contains(n3));
            assertFalse(sut.contains(n2));
        }

        @Test
        void linkThrowsNPEIfSrcIsNull() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.link(null, new Node("test")));
        }

        @Test
        void linkThrowsNPEIfDstIsNull() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.link(new Node("test"), null));
        }

        @Test
        void linkThrowsIAEIfSrcIsAbsent() {
            TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                    () -> sut.link(new Node("test"), new Node("test")));
        }

        @Test
        void getNodesLinkedToWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                    () -> sut.getNodesLinkedTo(null));
        }

        @Test
        void getNodesLinkedToAbsentNodeThrowsIAE() {
            TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                    () -> sut.getNodesLinkedTo(new Node("absent")));
        }


        @Test
        void getNodesLinkedToNodeWith1LinkReturnsAListOfSize1() {
            Node n1 = new Node("test");
            Node n2 = new Node("test");
            sut.add(n1);
            sut.add(n2);
            sut.link(n1, n2);
            assertEquals(1, sut.getNodesLinkedTo(n1).size());
        }

        @Test
        void getNodesLinkedToNodeInCycleReturnsCorrectNeighborList() {
            Node n1 = new Node("test");
            Node n2 = new Node("test");
            Node n3 = new Node("test");
            sut.add(n1);
            sut.add(n2);
            sut.add(n3);
            sut.link(n1, n2);
            sut.link(n2, n3);
            sut.link(n3, n1);
            List<Node> n1Neighbors = new ArrayList<>();
            n1Neighbors.add(n2);
            List<Node> n2Neighbors = new ArrayList<>();
            n2Neighbors.add(n3);
            List<Node> n3Neighbors = new ArrayList<>();
            n3Neighbors.add(n1);
            assertEquals(n1Neighbors, sut.getNodesLinkedTo(n1));
            assertEquals(n2Neighbors, sut.getNodesLinkedTo(n2));
            assertEquals(n3Neighbors, sut.getNodesLinkedTo(n3));
        }

        @Test
        void graphHasAUuidAfterCreation() {
            assertNotNull(sut.getUuid());
        }

        @Test
        void updateNullObserverThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.update(null));
        }

        @Test
        void subscribeWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.subscribe(null));
        }

        @Test
        void unsubscribeWithNullThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                    () -> sut.unsubscribe(null));
        }

        @Nested
        class AfterAddingAnObserver {
            DummyObserver obs;

            @BeforeEach
            void addAnObserver() {
                obs = new DummyObserver();
                sut.subscribe(obs);
            }

            @Test
            void updateWithUnsubscribedObserverThrowsIAE() {
                DummyObserver other = new DummyObserver();
                TestUtils.assertThrowsIAEWithMsg("Observer not subscribed",
                        () -> sut.update(other));
            }

            @Test
            void updateAfterSubscriptionUpdatesObserver() {
                sut.update(obs);
                assertTrue(obs.updated);
            }

            @Test
            void subscribeTwiceThrowsIAE() {
                TestUtils.assertThrowsIAEWithMsg("Observer already subscribed",
                        () -> sut.subscribe(obs));
            }

            @Test
            void updateObserversUpdatesEveryObserverSubscribed() {
                DummyObserver other = new DummyObserver();
                sut.subscribe(other);
                sut.updateObservers();
                assertTrue(obs.updated);
                assertTrue(other.updated);
            }

            @Test
            void observerSubscribedThenUnsubscribedIsNotUpdated() {
                sut.unsubscribe(obs);
                sut.updateObservers();
                assertFalse(obs.updated);
            }
        }

        @Nested
        class AfterAddingANode{
            Node node;

            @BeforeEach
            void addANode() {
                node = new Node("test");
                sut.add(node);
            }

            @Test
            void addTwiceThrowsIAE() {
                TestUtils.assertThrowsIAEWithMsg("Node already in graph",
                        () -> sut.add(node));
            }

            @Test
            void sizeOfGraphWith1NodeIs1() {
                assertEquals(1, sut.size());
            }

            @Test
            void sizeOfGraphWith1NodeAfterRemovalIs0() {
                assertEquals(1, sut.size());
                sut.remove(node);
                assertEquals(0, sut.size());
            }

            @Test
            void linkThrowsIAEIfDstIsAbsent() {
                TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                        () -> sut.link(node, new Node("test")));
            }

            @Test
            void linkThrowsIAEIfSrcAndDstAreEqual() {
                TestUtils.assertThrowsIAEWithMsg(
                  "Node can not be linked to itself",
                  () -> sut.link(node, node)
                );
            }

            @Test
            void getNodesLinkedToNodeWithNoLinksReturnsTheEmptyList() {
                assertTrue(sut.getNodesLinkedTo(node).isEmpty());
            }
        }
    }
}
